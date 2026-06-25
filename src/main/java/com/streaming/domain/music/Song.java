package com.streaming.domain.music;

public class Song {

    private Long id;
    private final String title;
    private final String artist;
    private final String album;
    private final int durationSeconds;

    private Song(Long id, String title, String artist, String album, int durationSeconds) {
        this.id = id;
        this.title = requireText(title, "title");
        this.artist = requireText(artist, "artist");
        this.album = album == null ? "" : album.trim();
        if (durationSeconds <= 0) {
            throw new IllegalArgumentException("durationSeconds must be positive");
        }
        this.durationSeconds = durationSeconds;
    }

    public static Song create(String title, String artist, String album, int durationSeconds) {
        return new Song(null, title, artist, album, durationSeconds);
    }

    public static Song reconstitute(Long id, String title, String artist, String album, int durationSeconds) {
        return new Song(id, title, artist, album, durationSeconds);
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }

    public void assignId(Long id) {
        this.id = id;
    }

    public Long id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String artist() {
        return artist;
    }

    public String album() {
        return album;
    }

    public int durationSeconds() {
        return durationSeconds;
    }
}
