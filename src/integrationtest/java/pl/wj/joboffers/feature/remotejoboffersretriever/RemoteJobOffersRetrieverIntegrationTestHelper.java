package pl.wj.joboffers.feature.remotejoboffersretriever;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.remotejoboffersretriever.dto.RemoteJobOfferDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RemoteJobOffersRetrieverIntegrationTestHelper {
    private final ObjectMapper objectMapper;

    private final List<RemoteJobOfferDto> remoteJobOfferDtos = new ArrayList<>() {
        {
            add(new RemoteJobOfferDto("Junior Java Developer", "CompanyName s.a.",
                    "3 000 - 5 000 PLN", "https://example.pl/job-offer-1"));
            add(new RemoteJobOfferDto("Junior Java Backend Developer", "CompanyName s.a.",
                    "5 000 - 8 000 PLN", "https://example.pl/job-offer-2"));
            add(new RemoteJobOfferDto("Fullstack Developer", "CompanyName s.a.",
                    "5 000 - 9 000 PLN", "https://example.pl/job-offer-3"));
            add(new RemoteJobOfferDto("Java Developer", "CompanyName s.a.",
                    "5 000 - 9 000 PLN", "https://example.pl/job-offer-4"));
        }
    };

    public String createBodyWithSomeJobOffers() {
        try {
            return objectMapper.writeValueAsString(remoteJobOfferDtos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "[]";
    }

    public Set<RemoteJobOfferDto> getRemoteJobOfferDtos() {
        return new HashSet<>(remoteJobOfferDtos);
    }
}
