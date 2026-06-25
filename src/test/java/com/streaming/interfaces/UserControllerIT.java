package com.streaming.interfaces;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("UserController (integration)")
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("creates a user and returns 201 with empty favorites")
    void createsUser() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Carol\",\"email\":\"carol@stream.io\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Carol")))
                .andExpect(jsonPath("$.email", is("carol@stream.io")))
                .andExpect(jsonPath("$.favoriteSongIds").isEmpty());
    }

    @Test
    @DisplayName("returns a seeded user by id")
    void findsSeededUser() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Alice Johnson")));
    }

    @Test
    @DisplayName("rejects an invalid email with a validation error")
    void rejectsInvalidEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Bad\",\"email\":\"not-an-email\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation Error")))
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    @DisplayName("rejects a duplicate email with a business rule violation")
    void rejectsDuplicateEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Clone\",\"email\":\"alice@stream.io\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Business Rule Violation")))
                .andExpect(jsonPath("$.message", is("email-already-registered")))
                .andExpect(jsonPath("$.path", is("/users")));
    }

    @Test
    @DisplayName("adds a favorite song to a user")
    void addsFavorite() throws Exception {
        mockMvc.perform(post("/users/2/favorites/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.favoriteSongIds", is(java.util.List.of(3))));
    }

    @Test
    @DisplayName("returns 404 when adding a favorite for a missing user")
    void favoriteMissingUser() throws Exception {
        mockMvc.perform(post("/users/999/favorites/3"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Resource Not Found")));
    }
}
