package com.streaming.application.music;

import com.streaming.domain.music.Song;
import com.streaming.domain.music.SongRepository;
import com.streaming.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SongApplicationService {

    private final SongRepository songRepository;

    public SongApplicationService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Transactional
    public Song create(String title, String artist, String album, int durationSeconds) {
        return songRepository.save(Song.create(title, artist, album, durationSeconds));
    }

    @Transactional(readOnly = true)
    public List<Song> findAll() {
        return songRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Song findById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song", id));
    }
}
