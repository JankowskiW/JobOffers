package pl.wj.joboffers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.time.Clock;

@Configuration
@Profile("integration")
public class IntegrationConfig {

    @Bean
    @Primary
    Clock clock() { // TODO: Change Clock to AdjustableClock
        return Clock.systemUTC();
    }

}
