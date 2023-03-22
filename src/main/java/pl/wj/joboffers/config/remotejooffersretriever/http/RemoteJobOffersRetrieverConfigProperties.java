package pl.wj.joboffers.config.remotejooffersretriever.http;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "job-offers.http.client.config.http")
@Builder
public record RemoteJobOffersRetrieverConfigProperties(
        int connectionTimeout, int readTimeout, String uri, int port) {
}
