package pl.wj.joboffers.config.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import pl.wj.joboffers.config.remotejooffersretriever.http.RemoteJobOffersRetrieverConfigProperties;
import pl.wj.joboffers.infrastructure.security.model.SecurityProperties;

@Configuration
@EnableConfigurationProperties({RemoteJobOffersRetrieverConfigProperties.class, SecurityProperties.class})
@PropertySource("classpath:security.properties")
public class PropertiesConfiguration {
}
