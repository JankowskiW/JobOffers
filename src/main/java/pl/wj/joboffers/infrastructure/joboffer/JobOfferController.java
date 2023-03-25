package pl.wj.joboffers.infrastructure.joboffer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-offers")
public class JobOfferController {

    @GetMapping("")
    public ResponseEntity<JobOfferResponseDto> getJobOfferById() {
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
