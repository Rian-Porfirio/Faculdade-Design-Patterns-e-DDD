package com.streaming.domain.music;

import com.streaming.shared.domain.AggregateRoot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Playlist extends AggregateRoot {

    private Long id;
    private final Long userId;
    private String name;
    private final List<Long> songIds;

    private Playlist(Long id, Long userId, String name, List<Long> songIds) {
        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }
        this.id = id;
        this.userId = userId;
        this.name = requireName(name);
        this.songIds = new ArrayList<>(songIds);
    }

    public static Playlist create(Long userId, String name) {
        return new Playlist(null, userId, name, new ArrayList<>());
    }

    public static Playlist reconstitute(Long id, Long userId, String name, List<Long> songIds) {
        return new Playlist(id, userId, name, songIds);
    }

    public void addSong(Long songId) {
        if (songId == null) {
            throw new IllegalArgumentException("songId must not be null");
        }
        if (!songIds.contains(songId)) {
            songIds.add(songId);
        }
    }

    public void removeSong(Long songId) {
        songIds.remove(songId);
    }

    public boolean contains(Long songId) {
        return songIds.contains(songId);
    }

    private static String requireName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        return name.trim();
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }

    public Long userId() {
        return userId;
    }

    public String name() {
        return name;
    }

    public List<Long> songIds() {
        return Collections.unmodifiableList(songIds);
    }
}
