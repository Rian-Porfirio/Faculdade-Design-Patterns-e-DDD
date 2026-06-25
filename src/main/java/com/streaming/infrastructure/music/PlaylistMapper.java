package com.streaming.infrastructure.music;

import com.streaming.domain.music.Playlist;

import java.util.ArrayList;

final class PlaylistMapper {

    private PlaylistMapper() {
    }

    static PlaylistJpaEntity toJpa(Playlist playlist) {
        PlaylistJpaEntity entity = new PlaylistJpaEntity();
        entity.setId(playlist.id());
        entity.setUserId(playlist.userId());
        entity.setName(playlist.name());
        entity.setSongIds(new ArrayList<>(playlist.songIds()));
        return entity;
    }

    static Playlist toDomain(PlaylistJpaEntity entity) {
        return Playlist.reconstitute(
                entity.getId(), entity.getUserId(), entity.getName(),
                new ArrayList<>(entity.getSongIds()));
    }
}
