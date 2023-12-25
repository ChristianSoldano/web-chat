package ar.com.christiansoldano.chat.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record RegisterDTO(

        @NotEmpty
        String username,

        @NotEmpty
        String password,

        @NotEmpty
        String name,

        @NotEmpty
        String lastname,

        @NotEmpty
        @Email
        String email
) {
}
