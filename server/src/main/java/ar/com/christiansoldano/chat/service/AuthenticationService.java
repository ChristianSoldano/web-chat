package ar.com.christiansoldano.chat.service;

import ar.com.christiansoldano.chat.configuration.auth.JwtUtils;
import ar.com.christiansoldano.chat.dto.auth.AuthenticationReponseDTO;
import ar.com.christiansoldano.chat.dto.auth.LoginDTO;
import ar.com.christiansoldano.chat.dto.auth.RegisterDTO;
import ar.com.christiansoldano.chat.exception.user.UserAlreadyExistsException;
import ar.com.christiansoldano.chat.mapper.UserMapper;
import ar.com.christiansoldano.chat.model.Role;
import ar.com.christiansoldano.chat.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private static final String COMMON_USER_ROLE = "COMMON";

    @Transactional
    public AuthenticationReponseDTO register(RegisterDTO registerDTO) {
        checkIfUserExists(registerDTO.username(), registerDTO.email());
        User user = createUser(registerDTO);
        userService.save(user);
        String token = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        return new AuthenticationReponseDTO(token, refreshToken);
    }

    public AuthenticationReponseDTO authenticate(LoginDTO loginDTO) {
        User user = userService.findUserByUsername(loginDTO.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        boolean passwordMatches = passwordEncoder.matches(loginDTO.password(), user.getPassword());
        if (!passwordMatches) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        return new AuthenticationReponseDTO(token, refreshToken);
    }

    public AuthenticationReponseDTO refreshToken(String refreshToken) {
        String username = jwtUtils.extractUsername(refreshToken);
        boolean isRefreshToken = jwtUtils.isRefreshToken(refreshToken);
        if (username == null || !isRefreshToken) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        User user = userService.findUserByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
        String token = jwtUtils.generateToken(user);

        return new AuthenticationReponseDTO(token, refreshToken);
    }

    private User createUser(RegisterDTO registerDTO) {
        User user = userMapper.mapFromRegisterDTO(registerDTO);
        user.setPassword(passwordEncoder.encode(registerDTO.password()));
        Role role = roleService.findRoleByName(COMMON_USER_ROLE);
        user.setRoles(List.of(role));

        return user;
    }

    private void checkIfUserExists(String username, String email) {
        boolean userAlreadyExist = userService.existsByUsernameOrEmail(username, email);
        if (userAlreadyExist) {
            throw new UserAlreadyExistsException("User already exists");
        }
    }
}
