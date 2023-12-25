package ar.com.christiansoldano.chat.dto.user;

import java.sql.Timestamp;
import java.util.List;

public record UserDTO(

        Integer id,
        String username,
        String password,
        String name,
        String lastname,
        String email,
        boolean enabled,
        Timestamp createdAt,
        Timestamp updatedAt,
        List<RoleDTO> roles
) {
}
