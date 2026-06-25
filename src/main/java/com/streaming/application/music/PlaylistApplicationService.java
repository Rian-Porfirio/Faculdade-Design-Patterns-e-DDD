package com.streaming.application.music;

import com.streaming.domain.identity.UserRepository;
import com.streaming.domain.music.Playlist;
import com.streaming.domain.music.PlaylistRepository;
import com.streaming.domain.music.SongRepository;
import com.streaming.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlaylistApplicationService {

    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;

    public PlaylistApplicationService(PlaylistRepository playlistRepository,
                                      SongRepository songRepository,
                                      UserRepository userRepository) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Playlist create(Long userId, String name) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new ResourceNotFoundException("User", userId);
        }
        return playlistRepository.save(Playlist.create(userId, name));
    }

    @Transactional
    public Playlist addSong(Long playlistId, Long songId) {
        Playlist playlist = findById(playlistId);
        ensureSongExists(songId);
        playlist.addSong(songId);
        return playlistRepository.save(playlist);
    }

    @Transactional
    public Playlist removeSong(Long playlistId, Long songId) {
        Playlist playlist = findById(playlistId);
        playlist.removeSong(songId);
        return playlistRepository.save(playlist);
    }

    @Transactional(readOnly = true)
    public Playlist findById(Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", id));
    }

    private void ensureSongExists(Long songId) {
        if (!songRepository.existsById(songId)) {
            throw new ResourceNotFoundException("Song", songId);
        }
    }
}
