package com.streaming.interfaces.music;

import com.streaming.domain.music.Playlist;

import java.util.List;

public record PlaylistResponse(
        Long id,
        Long userId,
        String name,
        List<Long> songIds
) {
    public static PlaylistResponse from(Playlist playlist) {
        return new PlaylistResponse(
                playlist.id(),
                playlist.userId(),
                playlist.name(),
                List.copyOf(playlist.songIds())
        );
    }
}
