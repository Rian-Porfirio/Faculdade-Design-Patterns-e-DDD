package com.streaming.interfaces.music;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateSongRequest(
        @NotBlank(message = "title must not be blank")
        String title,

        @NotBlank(message = "artist must not be blank")
        String artist,

        @NotBlank(message = "album must not be blank")
        String album,

        @Positive(message = "durationSeconds must be positive")
        int durationSeconds
) {
}
