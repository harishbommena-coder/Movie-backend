package com.moviereview.service;

import com.moviereview.dto.AuthRequest;
import com.moviereview.dto.AuthResponse;
import com.moviereview.dto.RegisterRequest;
import com.moviereview.dto.UserDto;
import com.moviereview.entity.Role;
import com.moviereview.entity.User;
import com.moviereview.config.JwtTokenProvider;
import com.moviereview.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username '" + request.getUsername() + "' is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email '" + request.getEmail() + "' is already registered");
        }

        User user = new User(
                request.getUsername().trim(),
                request.getEmail().trim().toLowerCase(),
                passwordEncoder.encode(request.getPassword()),
                Role.ROLE_USER
        );

        User saved = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(saved.getUsername(), saved.getRole().name());

        return new AuthResponse(token, saved.getId(), saved.getUsername(), saved.getEmail(), saved.getRole().name());
    }

    public AuthResponse authenticate(AuthRequest request) {
        String identifier = request.getUsernameOrEmail().trim();
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(identifier, identifier);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid username/email or password");
        }

        User user = userOpt.get();
        if (!user.isEnabled()) {
            throw new IllegalStateException("Your account has been deactivated. Please contact an administrator.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username/email or password");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail(), user.getRole().name());
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    public UserDto getUserProfile(String username) {
        User user = findByUsername(username);
        return new UserDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // Prevent deactivating admin account
        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new IllegalStateException("Administrator account cannot be deactivated");
        }

        user.setEnabled(!user.isEnabled());
        return new UserDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new IllegalStateException("Administrator account cannot be deleted");
        }

        userRepository.delete(user);
    }

    public long countUsers() {
        return userRepository.count();
    }
}
