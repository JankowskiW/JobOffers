package pl.wj.joboffers.infrastructure.remotejoboffersretriever.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.wj.joboffers.domain.remotejoboffersretriever.dto.RemoteJobOfferDto;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Log4j2
public class RemoteJobOffersHttpRetriever implements RemoteJobOffersRetriever {
    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

    private static final String SERVICE_PATH = "/offers";

    @Override
    public Set<RemoteJobOfferDto> retrieveRemoteJobOfferDtos() {
        try {
            ResponseEntity<Set<RemoteJobOfferDto>> response = executeGetRequest();
            return getBodyOrEmptySet(response);
        } catch (ResourceAccessException e) {
            log.error("Error occured during receiving job offers from remote service: " + e.getMessage());
            return new HashSet<>();
        }
    }

    private ResponseEntity<Set<RemoteJobOfferDto>> executeGetRequest() throws ResourceAccessException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        final String url = UriComponentsBuilder.fromHttpUrl(createServiceURL()).toUriString();
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {});
    }

    private String createServiceURL() {
        return String.format("%s:%s%s", uri, port, SERVICE_PATH);
    }

    private Set<RemoteJobOfferDto> getBodyOrEmptySet(ResponseEntity<Set<RemoteJobOfferDto>> response) {
        Set<RemoteJobOfferDto> remoteJobOfferDtos = new HashSet<>();
        if (response.getBody() != null) {
            remoteJobOfferDtos = new HashSet<>(response.getBody());
        }
        return remoteJobOfferDtos;
    }
}
