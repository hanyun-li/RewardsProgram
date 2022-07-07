package cloud.lihan.rewardsprogram.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hanyun.li
 * @createTime 2022/06/24 15:01:00
 */
@Configuration
public class ElasticsearchConfiguration {

    @Value("${elasticsearch.host}")
    private String host = "127.0.0.1";

    @Value("${elasticsearch.port}")
    private int port = 9200;

    @Value("${elasticsearch.connTimeout}")
    private int connTimeout;

    @Value("${elasticsearch.socketTimeout}")
    private int socketTimeout;

    @Value("${elasticsearch.connectionRequestTimeout}")
    private int connectionRequestTimeout;

    private final RestClient restClient = RestClient.builder(
            new HttpHost(host, port)).build();

    private final ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());

    @Bean(name = "esClient")
    public ElasticsearchClient initRestClient() {
        return new ElasticsearchClient(transport);
    }

//    @Bean(name = "esAsyncClient")
//    public ElasticsearchAsyncClient initAsyncRestClient() {
//        return new ElasticsearchAsyncClient(transport);
//    }

}
