package ar.com.christiansoldano.chat.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.UUID;

public record ChatDTO(

        UUID id,

        String username1,

        String username2,

        @JsonProperty("created_at")
        Timestamp createdAt
) {
}
