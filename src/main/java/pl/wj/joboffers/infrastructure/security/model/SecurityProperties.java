package pl.wj.joboffers.infrastructure.security.model;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "job-offers.security")
@Builder
public record SecurityProperties(String secretKey, long expirationDays, String issuer) {
}
