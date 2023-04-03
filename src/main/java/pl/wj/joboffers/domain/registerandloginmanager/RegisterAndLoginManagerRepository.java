package pl.wj.joboffers.domain.registerandloginmanager;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.wj.joboffers.domain.user.model.User;

@Repository
public interface RegisterAndLoginManagerRepository extends MongoRepository<User, Long> {
}
