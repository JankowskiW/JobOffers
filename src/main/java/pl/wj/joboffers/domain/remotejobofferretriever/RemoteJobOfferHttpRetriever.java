package pl.wj.joboffers.domain.remotejobofferretriever;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.wj.joboffers.domain.remotejobofferretriever.model.dto.RemoteJobOfferDto;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class RemoteJobOfferHttpRetriever implements RemoteJobOfferRetriever {
    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

    private static final String SERVICE_PATH = "/offers";

    @Override
    public Set<RemoteJobOfferDto> retrieveRemoteJobOffers() {
        ResponseEntity<Set<RemoteJobOfferDto>> response = executeGetRequest(SERVICE_PATH);
        return getBodyOrEmptySet(response);
    }

    private ResponseEntity<Set<RemoteJobOfferDto>> executeGetRequest(String servicePath) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        final String url = UriComponentsBuilder.fromHttpUrl(createServiceURL(servicePath)).toUriString();
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {});
    }

    private String createServiceURL(String servicePath) {
        return String.format("%s:%s%s", uri, port, servicePath);
    }

    private Set<RemoteJobOfferDto> getBodyOrEmptySet(ResponseEntity<Set<RemoteJobOfferDto>> response) {
        Set<RemoteJobOfferDto> remoteJobOffers = new HashSet<>();
        if (response.getBody() != null) {
            remoteJobOffers = new HashSet<>(response.getBody());
        }
        return remoteJobOffers;
    }
}
