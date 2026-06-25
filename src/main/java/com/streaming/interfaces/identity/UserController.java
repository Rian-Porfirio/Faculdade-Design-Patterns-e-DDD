package com.streaming.interfaces.identity;

import com.streaming.application.identity.UserApplicationService;
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

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User registration, lookup and favorite songs")
public class UserController {

    private final UserApplicationService userApplicationService;

    public UserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new user")
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        return UserResponse.from(userApplicationService.create(request.name(), request.email()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a user by id")
    public UserResponse findById(@PathVariable Long id) {
        return UserResponse.from(userApplicationService.findById(id));
    }

    @GetMapping
    @Operation(summary = "List all users")
    public List<UserResponse> findAll() {
        return userApplicationService.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    @PostMapping("/{id}/favorites/{songId}")
    @Operation(summary = "Add a song to the user's favorites")
    public UserResponse addFavorite(@PathVariable Long id, @PathVariable Long songId) {
        return UserResponse.from(userApplicationService.addFavorite(id, songId));
    }

    @DeleteMapping("/{id}/favorites/{songId}")
    @Operation(summary = "Remove a song from the user's favorites")
    public ResponseEntity<UserResponse> removeFavorite(@PathVariable Long id, @PathVariable Long songId) {
        return ResponseEntity.ok(UserResponse.from(userApplicationService.removeFavorite(id, songId)));
    }
}
