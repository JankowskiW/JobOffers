package pl.wj.joboffers.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import pl.wj.joboffers.domain.user.UserFacade;
import pl.wj.joboffers.exception.definition.ResourceNotFoundException;
import pl.wj.joboffers.infrastructure.security.model.SecurityMapper;

@RequiredArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {
    private final UserFacade userFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        try {
            return SecurityMapper.toSecurityUser(userFacade.getUserByUsername(username));
        } catch (ResourceNotFoundException e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }
}
