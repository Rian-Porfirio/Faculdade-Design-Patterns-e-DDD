package com.streaming.interfaces.music;

import com.streaming.application.music.PlaylistApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/playlists")
@Tag(name = "Playlists", description = "Playlist creation and song management")
public class PlaylistController {

    private final PlaylistApplicationService playlistApplicationService;

    public PlaylistController(PlaylistApplicationService playlistApplicationService) {
        this.playlistApplicationService = playlistApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new playlist for a user")
    public PlaylistResponse create(@Valid @RequestBody CreatePlaylistRequest request) {
        return PlaylistResponse.from(
                playlistApplicationService.create(request.userId(), request.name()));
    }

    @PostMapping("/{id}/songs/{songId}")
    @Operation(summary = "Add a song to a playlist")
    public PlaylistResponse addSong(@PathVariable Long id, @PathVariable Long songId) {
        return PlaylistResponse.from(playlistApplicationService.addSong(id, songId));
    }

    @DeleteMapping("/{id}/songs/{songId}")
    @Operation(summary = "Remove a song from a playlist")
    public ResponseEntity<PlaylistResponse> removeSong(@PathVariable Long id, @PathVariable Long songId) {
        return ResponseEntity.ok(
                PlaylistResponse.from(playlistApplicationService.removeSong(id, songId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a playlist by id")
    public PlaylistResponse findById(@PathVariable Long id) {
        return PlaylistResponse.from(playlistApplicationService.findById(id));
    }
}
