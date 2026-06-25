package com.streaming.interfaces.music;

import com.streaming.domain.music.Song;

public record SongResponse(
        Long id,
        String title,
        String artist,
        String album,
        int durationSeconds
) {
    public static SongResponse from(Song song) {
        return new SongResponse(
                song.id(),
                song.title(),
                song.artist(),
                song.album(),
                song.durationSeconds()
        );
    }
}
