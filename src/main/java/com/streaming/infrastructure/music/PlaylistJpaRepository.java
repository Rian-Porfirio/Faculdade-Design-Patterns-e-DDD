package com.streaming.infrastructure.music;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistJpaRepository extends JpaRepository<PlaylistJpaEntity, Long> {
}
