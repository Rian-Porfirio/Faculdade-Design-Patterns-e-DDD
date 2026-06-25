package com.streaming.domain.music;

import java.util.List;
import java.util.Optional;

public interface SongRepository {

    Song save(Song song);

    Optional<Song> findById(Long id);

    List<Song> findAll();

    boolean existsById(Long id);
}
