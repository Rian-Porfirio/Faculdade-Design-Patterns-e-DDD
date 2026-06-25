package com.streaming.infrastructure.music;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SongJpaRepository extends JpaRepository<SongJpaEntity, Long> {
}
