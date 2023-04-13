package pl.wj.joboffers.features;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.remotejoboffersretriever.dto.RemoteJobOfferDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class UserWantToGetJobOffersHelper {
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
            add(new RemoteJobOfferDto("Java Developer", "CompanyName s.a.",
                    "5 000 - 9 000 PLN", "https://example.pl/job-offer-5"));
        }
    };
    String createOffersBody(int numberOfOffers) {
        if (numberOfOffers > 0)
        {
            try {
                return objectMapper.writeValueAsString(remoteJobOfferDtos.stream().limit(numberOfOffers));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "[]";
    }

    List<RemoteJobOfferDto> getRemoteJobOfferDtos(int offset, int numberOfOffers) {
        return remoteJobOfferDtos.stream().limit(numberOfOffers).skip(offset).collect(Collectors.toList());
    }

}
