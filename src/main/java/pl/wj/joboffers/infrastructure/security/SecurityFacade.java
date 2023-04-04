package pl.wj.joboffers.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import pl.wj.joboffers.domain.user.model.dto.UserLoginRequestDto;
import pl.wj.joboffers.infrastructure.security.model.SecurityProperties;
import pl.wj.joboffers.infrastructure.security.model.dto.JwtResponseDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
@Log4j2
public class SecurityFacade {
    private final AuthenticationManager authenticationManager;
    private final SecurityProperties securityProperties;

    public JwtResponseDto login(UserLoginRequestDto userLoginRequestDto) {
        User user = getAuthenticatedUser(userLoginRequestDto.username(), userLoginRequestDto.password());
        return JwtResponseDto.builder()
                .token(createToken(user))
                .username(user.getUsername())
                .build();
    }

    private User getAuthenticatedUser(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return (User) authentication.getPrincipal();
    }

    private String createToken(User user) {
        // TODO: Create Clock bean and inject that bean to this class, also use that clock in LocalDateTime.now(clock) method
        Instant now = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(now.plusMillis(securityProperties.expirationMs()))
                .withIssuer(securityProperties.issuer())
                .sign(Algorithm.HMAC256(securityProperties.secretKey()));
    }
}
