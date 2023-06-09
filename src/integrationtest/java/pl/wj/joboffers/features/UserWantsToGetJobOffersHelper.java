package pl.wj.joboffers.features;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.joboffer.model.JobOfferMapper;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferResponseDto;
import pl.wj.joboffers.domain.remotejoboffersretriever.dto.RemoteJobOfferDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class UserWantsToGetJobOffersHelper {
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
            add(new RemoteJobOfferDto("Junior Java Developer", "CompanyName ltd",
                    "5 000 - 10 000 PLN", "https://example.pl/job-offer-5"));
        }
    };

    JobOfferResponseDto createNewJobOfferResponseDto(int index) {
        return JobOfferMapper.toJobOfferResponseDto(remoteJobOfferDtos.get(index));
    }

    String createNewOfferBody(int index) {
        try {
            return objectMapper.writeValueAsString(JobOfferMapper.toJobOfferRequestDto(remoteJobOfferDtos.get(index)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

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
