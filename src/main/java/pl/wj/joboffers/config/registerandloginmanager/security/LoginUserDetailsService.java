package pl.wj.joboffers.config.registerandloginmanager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.wj.joboffers.domain.registerandloginmanager.RegisterAndLoginManagerFacade;
import pl.wj.joboffers.domain.registerandloginmanager.model.SecurityMapper;

@RequiredArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {

    private final RegisterAndLoginManagerFacade registerAndLoginManagerFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        return SecurityMapper.toSecurityUser(registerAndLoginManagerFacade.getUserByUsername(username));
    }
}
