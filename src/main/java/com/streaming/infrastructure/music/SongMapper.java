package com.streaming.infrastructure.music;

import com.streaming.domain.music.Song;

final class SongMapper {

    private SongMapper() {
    }

    static SongJpaEntity toJpa(Song song) {
        return new SongJpaEntity(
                song.id(), song.title(), song.artist(), song.album(), song.durationSeconds());
    }

    static Song toDomain(SongJpaEntity entity) {
        return Song.reconstitute(
                entity.getId(), entity.getTitle(), entity.getArtist(),
                entity.getAlbum(), entity.getDurationSeconds());
    }
}
