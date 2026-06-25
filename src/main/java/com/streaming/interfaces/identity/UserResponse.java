package com.streaming.interfaces.identity;

import com.streaming.domain.identity.User;

import java.util.List;
import java.util.Set;

public record UserResponse(
        Long id,
        String name,
        String email,
        List<Long> favoriteSongIds
) {
    public static UserResponse from(User user) {
        Set<Long> favorites = user.favoriteSongIds();
        return new UserResponse(
                user.id(),
                user.name(),
                user.email().value(),
                favorites.stream().sorted().toList()
        );
    }
}
