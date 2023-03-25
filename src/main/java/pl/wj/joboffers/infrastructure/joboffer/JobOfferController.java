package pl.wj.joboffers.infrastructure.joboffer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-offers")
public class JobOfferController {

    @GetMapping("/{id}")
    public ResponseEntity<JobOfferResponseDto> getJobOfferById(@PathVariable String id) {
        return ResponseEntity.ok(new JobOfferResponseDto());
    }
}
