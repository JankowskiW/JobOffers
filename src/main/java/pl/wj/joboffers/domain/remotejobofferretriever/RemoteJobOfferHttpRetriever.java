package pl.wj.joboffers.domain.remotejobofferretriever;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.wj.joboffers.domain.remotejobofferretriever.model.RemoteJobOffer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class RemoteJobOfferHttpRetriever implements RemoteJobOfferRetriever {
    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

    @Override
    public Set<RemoteJobOffer> retrieveRemoteJobOffers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        final String url = UriComponentsBuilder.fromHttpUrl(createServiceURL("/job-offers"))
                .queryParam("someparam", "someValue")
                .toUriString();
        ResponseEntity<Set<RemoteJobOffer>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });
        return getBodyOrEmptySet(response);
    }

    private String createServiceURL(String servicePath) {
        return String.format("%s:%s%s", uri, port, servicePath);
    }

    private Set<RemoteJobOffer> getBodyOrEmptySet(ResponseEntity<Set<RemoteJobOffer>> response) {
        Set<RemoteJobOffer> remoteJobOffers = new HashSet<>();
        if (response.getBody() != null) {
            remoteJobOffers = new HashSet<>(response.getBody());
        }
        return remoteJobOffers;
    }
}
