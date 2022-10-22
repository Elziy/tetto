package com.elite.tetto.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ElasticSearchProperties.class)
public class ElasticSearchConfig {
    
    private final ElasticSearchProperties properties;
    
    public static final RequestOptions COMMON_OPTIONS;
    
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        // builder.addHeader("Authorization", "Bearer " + TOKEN);
        // builder.setHttpAsyncResponseConsumerFactory(
        //         new HttpAsyncResponseConsumerFactory
        //                 .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }
    
    public ElasticSearchConfig(ElasticSearchProperties properties) {
        this.properties = properties;
    }
    
    @Bean
    RestHighLevelClient client() {
        RestClientBuilder builder = RestClient
                .builder(new HttpHost(properties.getHost(), properties.getPort(), "http"));
        return new RestHighLevelClient(builder);
    }
}
