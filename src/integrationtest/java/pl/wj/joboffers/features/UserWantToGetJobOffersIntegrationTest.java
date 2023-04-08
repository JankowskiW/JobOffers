package pl.wj.joboffers.features;

import org.junit.jupiter.api.Test;
import pl.wj.joboffers.BaseIntegrationTest;

public class UserWantToGetJobOffersIntegrationTest  extends BaseIntegrationTest {

    @Test
    void shouldGetJobOffersWhenUserIsAuthenticatedAndExternalServiceHasSomeOffers() {
        // step 1: Unauthenticated user tried to get job offers and system should return FORBIDDEN(403)

        // step 2: User tried to log in with wrong credentials and system should return NOT_FOUND(404)

        // step 3: User tried to create account with username that already exists and system should return CONFLICT(409)

        // step 4: User created an account and system should return CREATED(201) and username

        // step 5: User tried to log in with existing credentials and system should return OK(200) and JWT Token

        // step 6: Authenticated user (with valid JWT) tried to get job offers before scheduler retrieve any offers
        //         from external service and system should return OK(200) with empty list of job offers

        // step 7: Scheduler tried to retrieve data from external service but there is none

        // step 8: Scheduler retrieved data from external service and there is 2 new job offers

        // step 9: Authenticated user tried to get job offers after scheduler retrieved data and system
        //         should return OK(200) and list with 2 offers

        // step 10: Scheduler retrieved 4 job offers from external service but stored only 2 because there is only
        //          2 new job offers

        // step 11: Authenticated user tried to get job offers after scheduler retrieved another part of data
        //          from external service and system should return OK(200) and list with 4 offers

        // step 12: Authenticated user tried to get specific job offer details but the id he sent does not exist in db
        //          and system should return NOT_FOUND(404)

        // step 13: Authenticated user tried to get specific job offer details and the id he sent does exist in db
        //          and system should return OK(200) and job offer details

        // step 14: Authenticated user tried to create new job offer but url he sent already exists in db and
        //          system should return CONFLICT(409)

        // step 15: Authenticated user tried to create new job offer and he sent correct body with url that does not
        //          exist in db yet and server should return CREATED(201) and body with created offer

        // step 16: Authenticated user tried to get job offers after he added new offer and system should return OK(200)
        //          and list with 5 offers

        // step 17: User with expired JWT token tried to get job offers and system should return FORBIDDEN(403)

    }
}
