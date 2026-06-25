package com.streaming.infrastructure.identity;

import com.streaming.domain.identity.Email;
import com.streaming.domain.identity.User;

import java.util.HashSet;

final class UserMapper {

    private UserMapper() {
    }

    static UserJpaEntity toJpa(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.id());
        entity.setName(user.name());
        entity.setEmail(user.email().value());
        entity.setFavoriteSongIds(new HashSet<>(user.favoriteSongIds()));
        return entity;
    }

    static User toDomain(UserJpaEntity entity) {
        return User.reconstitute(
                entity.getId(),
                entity.getName(),
                Email.of(entity.getEmail()),
                new HashSet<>(entity.getFavoriteSongIds()));
    }
}
