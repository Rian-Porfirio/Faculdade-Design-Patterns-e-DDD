package com.streaming.domain.identity;

import com.streaming.shared.domain.AggregateRoot;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class User extends AggregateRoot {

    private Long id;
    private String name;
    private Email email;
    private final Set<Long> favoriteSongIds;

    private User(Long id, String name, Email email, Set<Long> favoriteSongIds) {
        this.id = id;
        this.name = requireName(name);
        this.email = email;
        this.favoriteSongIds = new LinkedHashSet<>(favoriteSongIds);
    }

    public static User create(String name, Email email) {
        return new User(null, name, email, new LinkedHashSet<>());
    }

    public static User reconstitute(Long id, String name, Email email, Set<Long> favoriteSongIds) {
        return new User(id, name, email, favoriteSongIds);
    }

    public void addFavorite(Long songId) {
        favoriteSongIds.add(songId);
    }

    public void removeFavorite(Long songId) {
        favoriteSongIds.remove(songId);
    }

    public boolean hasFavorite(Long songId) {
        return favoriteSongIds.contains(songId);
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

    public String name() {
        return name;
    }

    public Email email() {
        return email;
    }

    public Set<Long> favoriteSongIds() {
        return Collections.unmodifiableSet(favoriteSongIds);
    }
}
