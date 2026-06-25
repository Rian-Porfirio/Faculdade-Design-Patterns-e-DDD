package com.streaming.interfaces.music;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePlaylistRequest(
        @NotNull(message = "userId must not be null")
        Long userId,

        @NotBlank(message = "name must not be blank")
        String name
) {
}
