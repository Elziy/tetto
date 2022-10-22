package com.elite.tetto.search.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.elite.tetto.common.entity.vo.es.AtlasESModel;
import com.elite.tetto.common.utils.R;
import com.elite.tetto.search.config.ElasticSearchConfig;
import com.elite.tetto.search.feign.ImageClient;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

// @SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ESTest {
    @Resource
    RestHighLevelClient client;
    
    @Resource
    private ImageClient imageClient;
    
    public void testIndexData() {
        R r = imageClient.getESData();
        List<AtlasESModel> data = r.getData(new TypeReference<List<AtlasESModel>>() {
        });
        
        data.forEach(atlasESModel -> {
            IndexRequest indexRequest = new IndexRequest("atlas");
            indexRequest.id(atlasESModel.getId().toString());
            indexRequest.source(JSON.toJSONString(atlasESModel), XContentType.JSON);
            try {
                IndexResponse index = client.index(indexRequest, ElasticSearchConfig.COMMON_OPTIONS);
                log.info("保存商品信息到es成功");
            } catch (IOException e) {
                log.error("保存商品信息到es失败: {}", e.getMessage());
                e.printStackTrace();
            }
        });
    }
}