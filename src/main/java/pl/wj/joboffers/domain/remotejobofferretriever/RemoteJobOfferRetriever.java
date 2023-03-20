package pl.wj.joboffers.domain.remotejobofferretriever;

import pl.wj.joboffers.domain.remotejobofferretriever.model.dto.RemoteJobOfferDto;

import java.util.Set;

public interface RemoteJobOfferRetriever {
    Set<RemoteJobOfferDto> retrieveRemoteJobOffers();
}
