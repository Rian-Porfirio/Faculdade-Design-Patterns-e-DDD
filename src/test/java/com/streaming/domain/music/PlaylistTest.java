package com.streaming.domain.music;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Playlist Aggregate")
class PlaylistTest {

    private Playlist newPlaylist() {
        return Playlist.create(1L, "Morning Vibes");
    }

    @Test
    @DisplayName("adds songs preserving insertion order and uniqueness")
    void addsSongs() {
        Playlist playlist = newPlaylist();
        playlist.addSong(10L);
        playlist.addSong(20L);
        playlist.addSong(10L);

        assertThat(playlist.songIds()).containsExactly(10L, 20L);
        assertThat(playlist.contains(20L)).isTrue();
    }

    @Test
    @DisplayName("removes songs")
    void removesSongs() {
        Playlist playlist = newPlaylist();
        playlist.addSong(10L);
        playlist.removeSong(10L);

        assertThat(playlist.contains(10L)).isFalse();
    }

    @Test
    @DisplayName("rejects null song id")
    void rejectsNullSong() {
        assertThatThrownBy(() -> newPlaylist().addSong(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("requires a user id")
    void requiresUserId() {
        assertThatThrownBy(() -> Playlist.create(null, "x"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
