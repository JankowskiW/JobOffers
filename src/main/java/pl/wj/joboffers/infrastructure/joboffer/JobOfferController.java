package pl.wj.joboffers.infrastructure.joboffer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wj.joboffers.domain.joboffer.JobOfferFacade;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferRequestDto;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/job-offers")
public class JobOfferController {

    private final JobOfferFacade jobOfferFacade;

    @GetMapping("/{id}")
    public ResponseEntity<JobOfferResponseDto> getJobOfferById(@PathVariable String id) {
        return ResponseEntity.ok(jobOfferFacade.getJobOfferById(id));
    }

    @GetMapping
    public ResponseEntity<List<JobOfferResponseDto>> getAllJobOffers() {
        return ResponseEntity.ok(jobOfferFacade.getAllJobOffers());
    }

    @PostMapping
    public ResponseEntity<JobOfferResponseDto> addJobOffer(@RequestBody @Valid JobOfferRequestDto jobOfferRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobOfferFacade.addJobOffer(jobOfferRequestDto));
    }
}
