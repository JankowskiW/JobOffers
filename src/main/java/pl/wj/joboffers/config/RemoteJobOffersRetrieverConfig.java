package pl.wj.joboffers.config;

import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.wj.joboffers.domain.remotejobofferretriever.RemoteJobOfferHttpRetriever;
import pl.wj.joboffers.domain.remotejobofferretriever.RemoteJobOfferRetriever;
import pl.wj.joboffers.exception.RestTemplateResponseErrorHandler;

import java.time.Duration;

@Configuration
public class RemoteJobOffersRetrieverConfig {
    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(@Value("${job-offers.http.client.config.connectionTimeout}") long connectionTimeout,
                                     @Value("${job-offers.http.client.config.readTimeout}") long readTimeout,
                                     RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {
        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(connectionTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .build();
    }

    @Bean
    public RemoteJobOfferRetriever remoteJobOfferRetriever(RestTemplate restTemplate,
                             @Value("${job-offers.http.client.config.uri}") String uri,
                             @Value("${job-offers.http.client.config.port}") int port) {
        return new RemoteJobOfferHttpRetriever(restTemplate, uri, port);
    }
}
