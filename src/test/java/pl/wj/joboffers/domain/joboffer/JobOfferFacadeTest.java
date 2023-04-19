package pl.wj.joboffers.domain.joboffer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wj.joboffers.domain.joboffer.model.JobOffer;
import pl.wj.joboffers.domain.joboffer.model.JobOfferMapper;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferDto;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferRequestDto;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferResponseDto;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferUrlDto;
import pl.wj.joboffers.exception.definition.ResourceNotFoundException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JobOfferFacadeTest {
    @Mock
    private JobOfferRepository jobOfferRepository;
    @InjectMocks
    private JobOfferFacade jobOfferFacade;

    @Test
    void shouldReturnSavedJobOffers() {
        // given
        Set<JobOfferDto> jobOfferDtos = new HashSet<>();
        jobOfferDtos.add(JobOfferDto.builder()
                .title("title")
                .company("company")
                .salary("salary")
                .offerUrl("offerUrl")
                .build());
        Set<JobOffer> jobOffers = JobOfferMapper.toJobOffersSet(jobOfferDtos);
        List<JobOffer> expectedResponse = jobOffers.stream().toList();
        given(jobOfferRepository.saveAll(anySet())).willReturn(expectedResponse);

        // when
        List<JobOffer> response = jobOfferFacade.saveJobOffers(jobOfferDtos);

        // then
        assertThat(response)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expectedResponse);
    }

    @Test
    void shouldReturnJobOffersWhenThereIsAny() {
        // given
        List<JobOffer> jobOffers = new ArrayList<>();
        jobOffers.add(createJobOffer());
        List<JobOfferResponseDto> expectedResponse = JobOfferMapper.toJobOfferResponseDtoList(jobOffers);
        given(jobOfferRepository.findAll()).willReturn(jobOffers);

        // when
        List<JobOfferResponseDto> response = jobOfferFacade.getAllJobOffers();

        // then
        assertThat(response)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expectedResponse);
    }

    @Test
    void shouldReturnEmptyListWhenThereIsNoJobOffers() {
        // given
        given(jobOfferRepository.findAll()).willReturn(new ArrayList<>());

        // when
        List<JobOfferResponseDto> response = jobOfferFacade.getAllJobOffers();

        // then
        assertThat(response)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldReturnSingleJobOfferWhenExists() {
        // given
        JobOffer jobOffer = createJobOffer();
        JobOfferResponseDto expectedResponse = JobOfferMapper.toJobOfferResponseDto(jobOffer);
        given(jobOfferRepository.findById(anyString())).willReturn(Optional.of(jobOffer));

        // when
        JobOfferResponseDto response = jobOfferFacade.getJobOfferById(jobOffer.id());

        // then
        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenJobOfferDoesNotExist() {
        // given
        String id = "notExistingId";
        given(jobOfferRepository.findById(anyString())).willReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> jobOfferFacade.getJobOfferById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Not found job offer with id:" + id);
    }

    @Test
    void shouldAddNewJobOffer() {
        // given
        JobOffer jobOffer = createJobOffer();
        JobOfferRequestDto jobOfferRequestDto = JobOfferRequestDto.builder()
                .title(jobOffer.title())
                .company(jobOffer.company())
                .salary(jobOffer.salary())
                .offerUrl(jobOffer.offerUrl())
                .build();
        JobOfferResponseDto expectedResponse = JobOfferMapper.toJobOfferResponseDto(jobOffer);
        given(jobOfferRepository.save(any(JobOffer.class))).willReturn(jobOffer);

        // when
        JobOfferResponseDto response = jobOfferFacade.addJobOffer(jobOfferRequestDto);

        // then
        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    void shouldReturnEmptyListWhenThereIsNoJobOffersInDatabase() {
        // given
        given(jobOfferRepository.findAllOfferUrls()).willReturn(new ArrayList<>());

        // when
        List<JobOfferUrlDto> response = jobOfferFacade.getAllJobOfferUrls();

        // then
        assertThat(response)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldReturnUrlsListWhenThereIsSomeJobOffersInDatabase() {
        // given
        List<JobOfferUrlDto> expectedResponse = new ArrayList<>();
        expectedResponse.add(new JobOfferUrlDto("url1"));
        expectedResponse.add(new JobOfferUrlDto("url2"));
        expectedResponse.add(new JobOfferUrlDto("url3"));
        given(jobOfferRepository.findAllOfferUrls()).willReturn(expectedResponse);

        // when
        List<JobOfferUrlDto> response = jobOfferFacade.getAllJobOfferUrls();

        // then
        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    private JobOffer createJobOffer() {
        return JobOffer.builder()
                .id("id")
                .title("title")
                .company("company")
                .salary("salary")
                .offerUrl("offerUrl")
                .build();
    }
}