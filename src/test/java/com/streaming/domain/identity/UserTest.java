package com.streaming.domain.identity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("User Aggregate")
class UserTest {

    private User newUser() {
        return User.create("Alice", Email.of("alice@example.com"));
    }

    @Test
    @DisplayName("starts with no favorites")
    void startsEmpty() {
        assertThat(newUser().favoriteSongIds()).isEmpty();
    }

    @Test
    @DisplayName("adds favorite songs without duplicates")
    void addsFavorites() {
        User user = newUser();
        user.addFavorite(1L);
        user.addFavorite(1L);
        user.addFavorite(2L);

        assertThat(user.favoriteSongIds()).containsExactly(1L, 2L);
        assertThat(user.hasFavorite(1L)).isTrue();
    }

    @Test
    @DisplayName("removes favorite songs")
    void removesFavorites() {
        User user = newUser();
        user.addFavorite(1L);
        user.removeFavorite(1L);

        assertThat(user.hasFavorite(1L)).isFalse();
    }

    @Test
    @DisplayName("favorites collection is unmodifiable")
    void favoritesUnmodifiable() {
        User user = newUser();
        assertThatThrownBy(() -> user.favoriteSongIds().add(99L))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("rejects blank name")
    void rejectsBlankName() {
        assertThatThrownBy(() -> User.create(" ", Email.of("a@b.com")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
