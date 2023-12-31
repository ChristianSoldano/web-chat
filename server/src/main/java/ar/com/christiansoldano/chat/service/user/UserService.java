package ar.com.christiansoldano.chat.service.user;

import ar.com.christiansoldano.chat.dto.user.UserDTO;
import ar.com.christiansoldano.chat.mapper.UserMapper;
import ar.com.christiansoldano.chat.model.user.User;
import ar.com.christiansoldano.chat.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();
        return userMapper.mapFromUsers(users);
    }

    public boolean existsByUsernameOrEmail(String username, String email) {
        return userRepository.existsByUsernameOrEmail(username, email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
