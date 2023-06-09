package pl.wj.joboffers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import pl.wj.joboffers.util.AdjustableClock;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

@Configuration
@Profile("integration")
public class IntegrationConfig {
    @Bean
    @Primary
    AdjustableClock clock() {

//        LocalDate localDate = LocalDate.of(2023, 3, 1);
//        LocalTime localTime = LocalTime.of(12,0);
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDate.now(zoneId).minusDays(1);
        LocalTime localTime = LocalTime.now(zoneId);
        return AdjustableClock.ofLocalDateAndLocalTime(localDate, localTime, zoneId);
    }

}
