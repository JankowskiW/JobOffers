package pl.wj.joboffers.domain.joboffer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.wj.joboffers.domain.joboffer.model.JobOffer;

@Repository
public interface JobOfferRepository extends MongoRepository<JobOffer, String> {
    boolean existsByOfferUrl(String offerUrl);
}
