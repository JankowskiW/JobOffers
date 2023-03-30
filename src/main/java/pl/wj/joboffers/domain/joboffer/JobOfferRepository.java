package pl.wj.joboffers.domain.joboffer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wj.joboffers.domain.joboffer.model.JobOffer;
import pl.wj.joboffers.domain.joboffer.model.dto.JobOfferUrlDto;

import java.util.List;

@Repository
public interface JobOfferRepository extends MongoRepository<JobOffer, String> {
    boolean existsByOfferUrl(String offerUrl);

    @Query("SELECT new pl.wj.joboffers.domain.joboffer.model.dto.JobOfferUrlDto(jo.offerUrl) FROM JobOffer jo")
    List<JobOfferUrlDto> findAllOfferUrls();
}
