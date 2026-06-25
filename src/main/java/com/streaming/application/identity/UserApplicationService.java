package com.streaming.application.identity;

import com.streaming.domain.identity.Email;
import com.streaming.domain.identity.User;
import com.streaming.domain.identity.UserRepository;
import com.streaming.domain.music.SongRepository;
import com.streaming.shared.exception.BusinessRuleViolationException;
import com.streaming.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserApplicationService {

    public static final String EMAIL_ALREADY_REGISTERED = "email-already-registered";

    private final UserRepository userRepository;
    private final SongRepository songRepository;

    public UserApplicationService(UserRepository userRepository, SongRepository songRepository) {
        this.userRepository = userRepository;
        this.songRepository = songRepository;
    }

    @Transactional
    public User create(String name, String email) {
        Email emailVo = Email.of(email);
        if (userRepository.existsByEmail(emailVo)) {
            throw new BusinessRuleViolationException(EMAIL_ALREADY_REGISTERED);
        }
        return userRepository.save(User.create(name, emailVo));
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User addFavorite(Long userId, Long songId) {
        User user = findById(userId);
        ensureSongExists(songId);
        user.addFavorite(songId);
        return userRepository.save(user);
    }

    @Transactional
    public User removeFavorite(Long userId, Long songId) {
        User user = findById(userId);
        user.removeFavorite(songId);
        return userRepository.save(user);
    }

    private void ensureSongExists(Long songId) {
        if (!songRepository.existsById(songId)) {
            throw new ResourceNotFoundException("Song", songId);
        }
    }
}
