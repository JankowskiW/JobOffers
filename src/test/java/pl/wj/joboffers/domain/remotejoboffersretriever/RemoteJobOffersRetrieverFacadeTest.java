package pl.wj.joboffers.domain.remotejoboffersretriever;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.joboffers.domain.joboffer.JobOfferFacade;
import pl.wj.joboffers.domain.joboffer.model.JobOffer;
import pl.wj.joboffers.domain.joboffer.model.JobOfferMapper;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferDto;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferUrlDto;
import pl.wj.joboffers.domain.remotejoboffersretriever.dto.RemoteJobOfferDto;
import pl.wj.joboffers.infrastructure.remotejoboffersretriever.http.RemoteJobOffersRetriever;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RemoteJobOffersRetrieverFacadeTest {
    @Mock
    private JobOfferFacade jobOfferFacade;
    @Mock
    private RemoteJobOffersRetriever remoteJobOffersRetriever;
    @InjectMocks
    private RemoteJobOffersRetrieverFacade remoteJobOffersRetrieverFacade;

    @Test
    void shouldRetrieveRemoteJobOffersWhenAnyWereRetrieved() {
        // given
        Set<RemoteJobOfferDto> expectedResponse = new HashSet<>();
        expectedResponse.add(RemoteJobOfferDto.builder()
                .title("title")
                .company("company")
                .salary("salary")
                .offerUrl("offerUrl")
                .build());
        given(remoteJobOffersRetriever.retrieveRemoteJobOfferDtos()).willReturn(expectedResponse);

        // when
        Set<RemoteJobOfferDto> response = remoteJobOffersRetrieverFacade.retrieveRemoteJobOfferDtos();

        // then
        assertThat(response)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expectedResponse);
    }

    @Test
    void shouldReturnEmptySetWhenNoneWereRetrieved() {
        // given
        given(remoteJobOffersRetriever.retrieveRemoteJobOfferDtos()).willReturn(new HashSet<>());

        // when
        Set<RemoteJobOfferDto> response = remoteJobOffersRetrieverFacade.retrieveRemoteJobOfferDtos();

        // then
        assertThat(response)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldSaveJobOffersIntoDatabaseWhenThereWasZeroJobOffers() {
        // given
        Set<RemoteJobOfferDto> remoteJobOffers = new HashSet<>();
        remoteJobOffers.add(RemoteJobOfferDto.builder()
                .title("title")
                .company("company")
                .salary("salary")
                .offerUrl("offerUrl")
                .build());
        Set<JobOfferDto> jobOfferDtos = JobOfferMapper.toJobOfferDtoSet(remoteJobOffers);
        List<JobOffer> jobOffers = JobOfferMapper.toJobOffersSet(jobOfferDtos).stream().toList();
        List<JobOfferDto> expectedResponse = JobOfferMapper.toJobOfferDtoList(jobOffers);
        given(jobOfferFacade.saveJobOffers(anySet())).willReturn(jobOffers);

        // when
        List<JobOfferDto> response = remoteJobOffersRetrieverFacade.saveRetrievedJobOffersIntoDatabase(remoteJobOffers);

        // then
        assertThat(response)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expectedResponse);
    }

    @Test
    void shouldSaveJobOffersIntoDatabaseWhenSomeJobOfferAlreadyExists() {
        // given
        Set<RemoteJobOfferDto> remoteJobOffers = new HashSet<>();
        RemoteJobOfferDto existingJobOffer = RemoteJobOfferDto.builder()
                .title("title1")
                .company("company1")
                .salary("salary1")
                .offerUrl("offerUrl1")
                .build();
        remoteJobOffers.add(existingJobOffer);
        remoteJobOffers.add(RemoteJobOfferDto.builder()
                .title("title2")
                .company("company2")
                .salary("salary2")
                .offerUrl("offerUrl2")
                .build());
        Set<JobOfferDto> jobOfferDtos = JobOfferMapper.toJobOfferDtoSet(remoteJobOffers);
        List<JobOffer> jobOffers = JobOfferMapper.toJobOffersSet(jobOfferDtos).stream().skip(1).toList();
        List<JobOfferDto> expectedResponse = JobOfferMapper.toJobOfferDtoList(jobOffers);
        List<JobOfferUrlDto> jobofferUrls = new ArrayList<>();
        jobofferUrls.add(new JobOfferUrlDto(existingJobOffer.offerUrl()));
        given(jobOfferFacade.getAllJobOfferUrls()).willReturn(jobofferUrls);
        given(jobOfferFacade.saveJobOffers(anySet())).willReturn(jobOffers);

        // when
        List<JobOfferDto> response = remoteJobOffersRetrieverFacade.saveRetrievedJobOffersIntoDatabase(remoteJobOffers);

        // then
        assertThat(response)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expectedResponse);
    }
}