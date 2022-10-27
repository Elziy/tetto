package com.elite.tetto.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.elite.tetto.common.constant.ESConstant;
import com.elite.tetto.common.entity.vo.es.AtlasESModel;
import com.elite.tetto.common.utils.DateUtil;
import com.elite.tetto.common.utils.PageUtils;
import com.elite.tetto.search.config.ElasticSearchConfig;
import com.elite.tetto.search.dao.SearchDao;
import com.elite.tetto.search.entity.SearchParam;
import com.elite.tetto.search.entity.SearchResult;
import com.elite.tetto.search.entity.vo.AtlasRes;
import com.elite.tetto.search.entity.vo.SuggestTags;
import com.elite.tetto.search.service.SearchService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    
    @Resource
    private SearchDao searchDao;
    
    @Resource
    RestHighLevelClient restHighLevelClient;
    
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;
    
    /**
     * 从es中检索图集
     *
     * @param searchParam 检索参数
     * @return {@link PageUtils}
     */
    @Override
    public SearchResult searchAtlasPage(SearchParam searchParam) {
        // 添加查询热榜
        this.addHotSearch(searchParam.getKeyword());
        // 1.准备检索请求
        SearchRequest searchRequest = buildSearchRequest(searchParam);
        try {
            // 2.执行检索请求
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
            // 3.分析响应数据封装成我们需要的格式
            SearchResult searchResult = buildSearchResult(searchResponse, searchParam);
            return searchResult;
        } catch (IOException e) {
            throw new RuntimeException("检索请求失败");
        }
    }
    
    /**
     * 从es中检索相关标签
     *
     * @param tag 标签
     * @return {@link PageUtils}
     */
    @Override
    public List<String> searchTags(String tag) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(ESConstant.ATLAS_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchPhraseQuery("tags", tag));
        boolQuery.must(QueryBuilders.matchPhraseQuery("isPublic", "1"));
        searchSourceBuilder.query(boolQuery);
        searchRequest.source(searchSourceBuilder);
        
        try {
            // 2.执行检索请求
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
            // 3.分析响应数据封装成我们需要的格式
            SearchHits hits = searchResponse.getHits();
            Map<String, Long> tagsMap = new HashMap<>();
            for (SearchHit hit : hits) {
                AtlasESModel atlasESModel = JSON.parseObject(hit.getSourceAsString(), AtlasESModel.class);
                // 设置标签
                List<String> tags = atlasESModel.getTags();
                for (String t : tags) {
                    if (tagsMap.containsKey(t)) {
                        tagsMap.put(t, tagsMap.get(t) + 1);
                    } else {
                        tagsMap.put(t, 1L);
                    }
                }
            }
            // 删除标签中的检索关键字
            tagsMap.remove("#" + tag);
            // 按value值取出标签前8个key
            List<String> tags = tagsMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(8)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            return tags;
        } catch (IOException e) {
            throw new RuntimeException("检索标签请求失败");
        }
    }
    
    /**
     * 获取联想标签
     *
     * @param tag 标签
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<SuggestTags> getSuggestTags(String tag) {
        List<SuggestTags> suggestTags = searchDao.getSuggestTags(tag);
        suggestTags.forEach(suggestTag -> {
            // 高亮关键词
            String suggestTagStr = suggestTag.getTag();
            // 去除#号
            suggestTagStr = suggestTagStr.substring(1);
            suggestTag.setTag(suggestTagStr);
            String suggest = suggestTagStr.replace(tag, "<em class=\"suggest_high_light\">" + tag + "</em>");
            suggestTag.setSuggest(suggest);
        });
        return suggestTags;
    }
    
    /**
     * 添加检索热榜
     *
     * @param keyword 关键字
     */
    @Override
    public void addHotSearch(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return;
        }
        try {
            String tag = searchDao.getTag("#" + keyword);
            if (StringUtils.isEmpty(tag)) {
                return;
            }
            // 去掉#号
            tag = tag.substring(1);
            Date nowDate = DateUtil.getNowDate();
            String hour = DateUtil.format(nowDate, "yyyy_MM_dd_HH");
            String key = ESConstant.ATLAS_HOT_SEARCH + hour;
            redisTemplate.opsForZSet().incrementScore(key, tag, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取热搜榜
     *
     * @return {@link List}<{@link String}>
     */
    @Override
    public Set<String> getHotSearch() {
        Date nowDate = DateUtil.getNowDate();
        // 获取上一个小时
        Date lastHour = DateUtil.offsetHour(nowDate, -1);
        String lastHourStr = DateUtil.format(lastHour, "yyyy_MM_dd_HH");
        String key = ESConstant.ATLAS_HOT_SEARCH + lastHourStr;
        Set<String> tags = redisTemplate.opsForZSet().reverseRange(key, 0, ESConstant.ATLAS_HOT_SEARCH_SIZE - 1);
        return tags;
    }
    
    private SearchRequest buildSearchRequest(SearchParam searchParam) {
        String keyword = searchParam.getKeyword();
        if (Objects.isNull(keyword) || keyword.isEmpty()) {
            throw new RuntimeException("检索关键字不能为空");
        }
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(ESConstant.ATLAS_INDEX);
        
        // 用来构建DSL语句
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        
        // 1.构建检索条件
        buildSearchCondition(searchParam, searchSourceBuilder);
        
        // 2.构建构建排序、分页、高亮条件
        buildOtherConditions(searchParam, searchSourceBuilder);
        
        // 3.构建聚合
        buildAggregates(searchSourceBuilder);
        
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }
    
    /**
     * 构建检索条件
     *
     * @param searchParam         检索参数
     * @param searchSourceBuilder 检索源构建器
     */
    private void buildSearchCondition(SearchParam searchParam, SearchSourceBuilder searchSourceBuilder) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 1.1 构建检索关键字
        boolQuery.must(QueryBuilders.matchPhraseQuery("tags", searchParam.getKeyword()));
        boolQuery.must(QueryBuilders.matchPhraseQuery("isPublic", "1"));
        boolQuery.should(QueryBuilders.matchPhraseQuery("tags", searchParam.getKeyword()));
        boolQuery.should(QueryBuilders.matchPhraseQuery("tags", searchParam.getKeyword()));
        searchSourceBuilder.query(boolQuery);
    }
    
    /**
     * 构建构建排序、分页
     *
     * @param searchParam         检索参数
     * @param searchSourceBuilder 检索源构建器
     */
    private void buildOtherConditions(SearchParam searchParam, SearchSourceBuilder searchSourceBuilder) {
        if (!StringUtils.isEmpty(searchParam.getSort())) {
            String sort = searchParam.getSort();
            searchSourceBuilder.sort(sort, SortOrder.DESC);
        } else {
            searchSourceBuilder.sort("date", SortOrder.DESC);
        }
        
        if (Objects.nonNull(searchParam.getPage())) {
            searchSourceBuilder.from((searchParam.getPage() - 1) * ESConstant.ATLAS_PAGE_SIZE);
            searchSourceBuilder.size(ESConstant.ATLAS_PAGE_SIZE);
        }
    }
    
    /**
     * 构建聚合
     *
     * @param searchSourceBuilder 检索源构建器
     */
    private void buildAggregates(SearchSourceBuilder searchSourceBuilder) {
        // // 标签聚合
        // TermsAggregationBuilder tags = AggregationBuilders.terms("tags").field("tags").size(8);
        // searchSourceBuilder.aggregation(tags);
    }
    
    private SearchResult buildSearchResult(SearchResponse searchResponse, SearchParam searchParam) {
        SearchResult searchResult = new SearchResult();
        SearchHits hits = searchResponse.getHits();
        // 设置分页信息
        long total = hits.getTotalHits().value;
        int totalPage = Math.toIntExact((total + ESConstant.ATLAS_PAGE_SIZE - 1) / ESConstant.ATLAS_PAGE_SIZE);
        searchResult.setCurrPage(searchParam.getPage());
        searchResult.setPageSize(ESConstant.ATLAS_PAGE_SIZE);
        searchResult.setTotalCount(total);
        searchResult.setTotalPage(totalPage);
        // 设置检索结果
        List<AtlasRes> atlasResList = new ArrayList<>();
        for (SearchHit hit : hits) {
            AtlasESModel atlasESModel = JSON.parseObject(hit.getSourceAsString(), AtlasESModel.class);
            // 设置图集信息
            AtlasRes atlasRes = new AtlasRes();
            atlasRes.setUsername(atlasESModel.getUsername());
            atlasRes.setAvatar(atlasESModel.getAvatar());
            atlasRes.setId(atlasESModel.getId());
            atlasRes.setUId(atlasESModel.getUid());
            atlasRes.setTitle(atlasESModel.getTitle());
            atlasRes.setIntroduce(atlasESModel.getIntroduce());
            atlasRes.setIsPublic(atlasESModel.getIsPublic());
            atlasRes.setThumbnailUrl(atlasESModel.getThumbnailUrl());
            atlasRes.setDate(atlasESModel.getDate());
            atlasResList.add(atlasRes);
        }
        searchResult.setAtlas(atlasResList);
        return searchResult;
    }
}
