package pl.wj.joboffers.infrastructure.remotejoboffersretriever.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.remotejoboffersretriever.RemoteJobOffersRetrieverFacade;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.model.dto.RemoteJobOfferDto;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Log4j2
public class RemoteJobOffersRetrievingScheduler {
    private final RemoteJobOffersRetrieverFacade remoteJobOffersRetrieverFacade;

    @Scheduled(fixedDelayString = "${job-offers.http.client.config.scheduler.delay}")
    public void retrieveRemoteJobOfferDtos() {
        Set<RemoteJobOfferDto> remoteJobOfferDtos = remoteJobOffersRetrieverFacade.retrieveRemoteJobOfferDtos();
        log.info("Ilość pobranych rekordów z zdalnego repozytorium: " + remoteJobOfferDtos.size());
        remoteJobOffersRetrieverFacade.saveRetrievedJobOffersIntoDatabase(remoteJobOfferDtos);
    }
}
