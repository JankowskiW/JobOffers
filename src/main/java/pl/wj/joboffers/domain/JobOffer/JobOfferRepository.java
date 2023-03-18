package pl.wj.joboffers.domain.JobOffer;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.wj.joboffers.domain.JobOffer.model.JobOffer;

@Repository
public interface JobOfferRepository extends MongoRepository<JobOffer, String> {

}
