package pl.wj.joboffers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.wj.joboffers.config.remotejooffersretriever.http.RemoteJobOffersRetrieverConfigProperties;

@EnableConfigurationProperties({RemoteJobOffersRetrieverConfigProperties.class})
@EnableScheduling
@SpringBootApplication
public class JobOffersApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobOffersApplication.class, args);
    }
}
