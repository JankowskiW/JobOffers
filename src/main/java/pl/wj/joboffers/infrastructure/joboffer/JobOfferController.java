package pl.wj.joboffers.infrastructure.joboffer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wj.joboffers.domain.joboffer.JobOfferFacade;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-offers")
public class JobOfferController {

    private final JobOfferFacade jobOfferFacade;

    @GetMapping("/{id}")
    public ResponseEntity<JobOfferResponseDto> getJobOfferById(@PathVariable String id) {
        JobOfferResponseDto response = JobOfferResponseDto.builder()
                .id("identifier")
                .title("some title")
                .company("some company")
                .salary("some salary")
                .offerUrl("some offer url")
                .build();
        return ResponseEntity.ok(response);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
