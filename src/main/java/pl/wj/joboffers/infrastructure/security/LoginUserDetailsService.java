package pl.wj.joboffers.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.wj.joboffers.domain.user.UserFacade;
import pl.wj.joboffers.infrastructure.security.model.SecurityMapper;

@RequiredArgsConstructor
@Log4j2
public class LoginUserDetailsService implements UserDetailsService {
    private final UserFacade userFacade;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return SecurityMapper.toSecurityUser(userFacade.getUserSecurityByUsername(username));
    }
}
