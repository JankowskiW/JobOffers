package pl.wj.joboffers.infrastructure.remotejoboffersretriever.http;

import pl.wj.joboffers.domain.remotejoboffersretriever.dto.RemoteJobOfferDto;

import java.util.Set;

public interface RemoteJobOffersRetriever {
    Set<RemoteJobOfferDto> retrieveRemoteJobOfferDtos();
}
