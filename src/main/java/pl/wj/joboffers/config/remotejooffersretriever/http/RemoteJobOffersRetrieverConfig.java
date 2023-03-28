package pl.wj.joboffers.config.remotejooffersretriever.http;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.RemoteJobOffersHttpRetriever;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.RemoteJobOffersRetriever;
import pl.wj.joboffers.exception.handler.RestTemplateResponseErrorHandler;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RemoteJobOffersRetrieverConfig {
    private final RemoteJobOffersRetrieverConfigProperties properties;

    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {
        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(properties.connectionTimeout()))
                .setReadTimeout(Duration.ofMillis(properties.readTimeout()))
                .build();
    }

    @Bean
    public RemoteJobOffersRetriever remoteJobOfferRetriever(RestTemplate restTemplate) {
        return new RemoteJobOffersHttpRetriever(restTemplate, properties.uri(), properties.port());
    }
}
