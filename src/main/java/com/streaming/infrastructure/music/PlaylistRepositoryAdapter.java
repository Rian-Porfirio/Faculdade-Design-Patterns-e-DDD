package com.streaming.infrastructure.music;

import com.streaming.domain.music.Playlist;
import com.streaming.domain.music.PlaylistRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PlaylistRepositoryAdapter implements PlaylistRepository {

    private final PlaylistJpaRepository jpaRepository;

    public PlaylistRepositoryAdapter(PlaylistJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Playlist save(Playlist playlist) {
        return PlaylistMapper.toDomain(jpaRepository.save(PlaylistMapper.toJpa(playlist)));
    }

    @Override
    public Optional<Playlist> findById(Long id) {
        return jpaRepository.findById(id).map(PlaylistMapper::toDomain);
    }
}
