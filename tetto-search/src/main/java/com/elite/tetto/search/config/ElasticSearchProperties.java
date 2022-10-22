package com.elite.tetto.search.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchProperties {
    
    private String host = "localhost";
    
    private int port = 9200;
}
