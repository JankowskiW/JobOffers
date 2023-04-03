package pl.wj.joboffers.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.user.model.User;
import pl.wj.joboffers.domain.user.model.dto.UserLoginRequestDto;
import pl.wj.joboffers.infrastructure.security.model.dto.JwtResponseDto;

@Component
@RequiredArgsConstructor
@Log4j2
public class SecurityFacade {
    private final AuthenticationManager authenticationManager;

    public JwtResponseDto login(UserLoginRequestDto userLoginRequestDto) {
        User user = getAuthenticatedUser(userLoginRequestDto.username(), userLoginRequestDto.password());
        return JwtResponseDto.builder()
                .token(createToken(user))
                .username(user.username())
                .build();
    }

    private User getAuthenticatedUser(String username, String password) {
        log.info("Username: " + username);
        log.info("Password: " + password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return (User) authentication.getPrincipal();
    }

    private String createToken(User user) {
        // todo: implement token creation
        return "TOKEN ;)";
    }
}
