package ar.com.christiansoldano.chat.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticationReponseDTO(

        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("refresh_token")
        String refreshToken
) {
}
