package pl.wj.joboffers.infrastructure.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wj.joboffers.domain.user.UserFacade;
import pl.wj.joboffers.domain.user.model.dto.UserRegisterRequestDto;
import pl.wj.joboffers.domain.user.model.dto.UserResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class UserController {
    private final UserFacade userFacade;


    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        return ResponseEntity.ok(userFacade.addUser(userRegisterRequestDto));
    }
}
