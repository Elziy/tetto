package com.elite.tetto.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.elite.tetto.common.constant.ESConstant;
import com.elite.tetto.common.entity.vo.es.AtlasESModel;
import com.elite.tetto.search.config.ElasticSearchConfig;
import com.elite.tetto.search.service.SaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
@Slf4j
public class SaveServiceImpl implements SaveService {
    
    @Resource
    RestHighLevelClient client;
    
    /**
     * 保存图集到es
     *
     * @param atlasESModel 图集
     * @return boolean
     */
    @Override
    public boolean saveAtlas(AtlasESModel atlasESModel) {
        IndexRequest indexRequest = new IndexRequest(ESConstant.ATLAS_INDEX);
        indexRequest.id(atlasESModel.getId().toString());
        indexRequest.source(JSON.toJSONString(atlasESModel), XContentType.JSON);
        try {
            IndexResponse index = client.index(indexRequest, ElasticSearchConfig.COMMON_OPTIONS);
            log.info("保存图集信息到es成功");
            return true;
        } catch (IOException e) {
            log.error("保存图集信息到es失败: {}", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 删除图集
     *
     * @param aid 图集id
     * @return boolean
     */
    @Override
    public boolean deleteAtlas(Long aid) {
        DeleteRequest deleteRequest = new DeleteRequest(ESConstant.ATLAS_INDEX, aid.toString());
        try {
            client.delete(deleteRequest, ElasticSearchConfig.COMMON_OPTIONS);
            log.info("删除图集信息成功");
            return true;
        } catch (IOException e) {
            log.error("删除图集信息失败: {}", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
