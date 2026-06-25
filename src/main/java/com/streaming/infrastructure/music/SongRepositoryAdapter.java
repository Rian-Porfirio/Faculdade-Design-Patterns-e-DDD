package com.streaming.infrastructure.music;

import com.streaming.domain.music.Song;
import com.streaming.domain.music.SongRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SongRepositoryAdapter implements SongRepository {

    private final SongJpaRepository jpaRepository;

    public SongRepositoryAdapter(SongJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Song save(Song song) {
        return SongMapper.toDomain(jpaRepository.save(SongMapper.toJpa(song)));
    }

    @Override
    public Optional<Song> findById(Long id) {
        return jpaRepository.findById(id).map(SongMapper::toDomain);
    }

    @Override
    public List<Song> findAll() {
        return jpaRepository.findAll().stream().map(SongMapper::toDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
