package com.elite.tetto.search.controller;

import com.elite.tetto.common.exception.ExceptionCode;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.search.entity.SearchParam;
import com.elite.tetto.search.entity.SearchResult;
import com.elite.tetto.search.entity.vo.SuggestTags;
import com.elite.tetto.search.service.SearchService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/search")
public class SearchController {
    
    @Resource
    private SearchService searchService;
    
    /**
     * 检索图集
     *
     * @param param 参数
     * @return {@link R}
     */
    @PostMapping("/atlas")
    public R searchEs(@RequestBody SearchParam param) {
        SearchResult searchResult;
        try {
            searchResult = searchService.searchAtlasPage(param);
        } catch (Exception e) {
            return R.error(ExceptionCode.UNKNOWN_EXCEPTION.getCode(), e.getMessage());
        }
        return R.ok().put("data", searchResult);
    }
    
    /**
     * 检索相关标签
     *
     * @param tag 标签
     * @return {@link R}
     */
    @GetMapping("/tags/related/{tag}")
    public R searchRelatedTags(@PathVariable String tag) {
        List<String> tags;
        try {
            tags = searchService.searchTags(tag);
        } catch (Exception e) {
            return R.error(ExceptionCode.UNKNOWN_EXCEPTION.getCode(), e.getMessage());
        }
        return R.ok().put("data", tags);
    }
    
    /**
     * 检索建议标签
     *
     * @param tag 标签
     * @return {@link R}
     */
    @GetMapping("/tags/suggest/{tag}")
    public R searchSuggestTags(@PathVariable String tag) {
        List<SuggestTags> tags;
        try {
            tags = searchService.getSuggestTags(tag);
        } catch (Exception e) {
            return R.error(ExceptionCode.UNKNOWN_EXCEPTION.getCode(), e.getMessage());
        }
        return R.ok().put("data", tags);
    }
    
    /**
     * 获取热搜榜
     *
     * @return {@link R}
     */
    @GetMapping("/hotSearch")
    public R getHotSearch() {
        Set<String> hotSearch = searchService.getHotSearch();
        return R.ok().put("data", hotSearch);
    }
}
