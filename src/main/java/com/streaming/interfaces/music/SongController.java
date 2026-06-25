package com.streaming.interfaces.music;

import com.streaming.application.music.SongApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/songs")
@Tag(name = "Songs", description = "Song catalog management")
public class SongController {

    private final SongApplicationService songApplicationService;

    public SongController(SongApplicationService songApplicationService) {
        this.songApplicationService = songApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new song in the catalog")
    public SongResponse create(@Valid @RequestBody CreateSongRequest request) {
        return SongResponse.from(songApplicationService.create(
                request.title(), request.artist(), request.album(), request.durationSeconds()));
    }

    @GetMapping
    @Operation(summary = "List all songs")
    public List<SongResponse> findAll() {
        return songApplicationService.findAll().stream()
                .map(SongResponse::from)
                .toList();
    }
}
