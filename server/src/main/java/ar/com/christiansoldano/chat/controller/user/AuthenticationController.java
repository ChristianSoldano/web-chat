package ar.com.christiansoldano.chat.controller.user;

import ar.com.christiansoldano.chat.dto.auth.AuthenticationReponseDTO;
import ar.com.christiansoldano.chat.dto.auth.LoginDTO;
import ar.com.christiansoldano.chat.dto.auth.RegisterDTO;
import ar.com.christiansoldano.chat.service.user.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";

    @PostMapping("/register")
    public ResponseEntity<AuthenticationReponseDTO> register(@RequestBody @Validated RegisterDTO registerDTO) {
        return ResponseEntity.ok(authenticationService.register(registerDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationReponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(authenticationService.authenticate(loginDTO));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@NonNull HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(TOKEN_BEARER_PREFIX)) {
            return new ResponseEntity<>(UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        return ResponseEntity.ok(authenticationService.refreshToken(token));
    }
}
