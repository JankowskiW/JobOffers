package pl.wj.joboffers.config;

import pl.wj.joboffers.config.remotejooffersretriever.http.RemoteJobOffersRetrieverConfig;
import pl.wj.joboffers.config.remotejooffersretriever.http.RemoteJobOffersRetrieverConfigProperties;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.RemoteJobOffersRetriever;

public class IsolationIntegrationConfig extends RemoteJobOffersRetrieverConfig {
    public IsolationIntegrationConfig(int port, int connectionTimeout, int readTimeout) {
        super(RemoteJobOffersRetrieverConfigProperties.builder()
                .uri("http://127.0.0.1")
                .port(port)
                .connectionTimeout(connectionTimeout)
                .readTimeout(readTimeout)
                .build());
    }

    public RemoteJobOffersRetriever remoteJobOfferRetriever() {
        return remoteJobOfferRetriever(restTemplate(restTemplateResponseErrorHandler()));
    }
}
