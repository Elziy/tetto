package com.elite.tetto.search.service.impl;

import com.elite.tetto.search.entity.SearchParam;
import com.elite.tetto.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SearchServiceTest {
    
    @Resource
    SearchService searchService;
    
    @Test
    public void searchAtlasPage() {
        SearchParam searchParam = new SearchParam();
        searchParam.setKeyword("三国");
        searchService.searchAtlasPage(searchParam);
    }
}