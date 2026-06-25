package com.streaming.domain.music;

import java.util.Optional;

public interface PlaylistRepository {

    Playlist save(Playlist playlist);

    Optional<Playlist> findById(Long id);
}
