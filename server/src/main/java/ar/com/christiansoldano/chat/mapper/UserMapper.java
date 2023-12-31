package ar.com.christiansoldano.chat.mapper;

import ar.com.christiansoldano.chat.dto.auth.RegisterDTO;
import ar.com.christiansoldano.chat.dto.user.UserDTO;
import ar.com.christiansoldano.chat.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface UserMapper {

    UserDTO mapFromUser(User user);

    List<UserDTO> mapFromUsers(List<User> users);

    @Mapping(target = "password", ignore = true)
    User mapFromRegisterDTO(RegisterDTO registerDTO);
}
