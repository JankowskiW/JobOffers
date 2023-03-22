package pl.wj.joboffers.domain.remotejobofferretriever;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.RemoteJobOffersRetriever;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.model.dto.RemoteJobOfferDto;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Log4j2
public class RemoteJobOfferRetrieverFacade {
    private final RemoteJobOffersRetriever remoteJobOffersRetriever;

    public void retrieveRemoteJobOffers() {
        Set<RemoteJobOfferDto> remoteJobOffers = remoteJobOffersRetriever.retrieveRemoteJobOffers();
       log.info("Number of retrieved job offers: " + remoteJobOffers.size());
    }
}
