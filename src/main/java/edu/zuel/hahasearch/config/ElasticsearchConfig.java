//package edu.zuel.hahasearch.config;
//
//import lombok.Data;
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
//import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
//
//

/**
 * 这个配置类好像用不到，删了也没影响
 */

//@ConfigurationProperties(prefix = "spring.elasticsearch")
//@Configuration
//@Data
//public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {
//    @Override
//    @Bean
//    public RestHighLevelClient elasticsearchClient() {
//
//        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo("localhost:9200")
//                .build();
//
//        return RestClients.create(clientConfiguration).rest();
//    }
//}