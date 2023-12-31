package ar.com.christiansoldano.chat.dto.user;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public record UserDTO(

        UUID id,
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
