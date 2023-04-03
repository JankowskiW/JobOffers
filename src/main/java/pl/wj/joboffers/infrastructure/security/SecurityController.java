package pl.wj.joboffers.infrastructure.security;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.wj.joboffers.domain.user.model.dto.UserLoginRequestDto;
import pl.wj.joboffers.infrastructure.security.model.dto.JwtResponseDto;

@RestController
@RequiredArgsConstructor
@Log4j2
public class SecurityController {
    private final SecurityFacade securityFacade;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
        return ResponseEntity.ok(securityFacade.login(userLoginRequestDto));
    }
}
