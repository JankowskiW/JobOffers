package pl.wj.joboffers.infrastructure.registerandloginmanager;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wj.joboffers.domain.registerandloginmanager.RegisterAndLoginManagerFacade;
import pl.wj.joboffers.domain.registerandloginmanager.model.dto.JwtResponseDto;
import pl.wj.joboffers.domain.user.model.dto.UserLoginRequestDto;
import pl.wj.joboffers.domain.user.model.dto.UserRegisterRequestDto;
import pl.wj.joboffers.domain.user.model.dto.UserResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class RegisterAndLoginManagerController {

    private final RegisterAndLoginManagerFacade registerAndLoginManagerFacade;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        return ResponseEntity.ok(registerAndLoginManagerFacade.registerUser(userRegisterRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> loginUser(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
        return ResponseEntity.ok(registerAndLoginManagerFacade.loginUser(userLoginRequestDto));
    }
}
