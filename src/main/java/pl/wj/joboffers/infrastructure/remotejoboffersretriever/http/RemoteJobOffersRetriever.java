package pl.wj.joboffers.infrastructure.remotejoboffersretriever.http;

import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.model.dto.RemoteJobOfferDto;

import java.util.Set;

public interface RemoteJobOffersRetriever {
    Set<RemoteJobOfferDto> retrieveRemoteJobOffers();
}
