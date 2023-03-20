package pl.wj.joboffers.domain.remotejobofferretriever;

import pl.wj.joboffers.domain.remotejobofferretriever.model.RemoteJobOffer;

import java.util.Set;

public interface RemoteJobOfferRetriever {
    Set<RemoteJobOffer> retrieveRemoteJobOffers();
}
