package com.example.shop.service;

import com.example.shop.model.User;
import com.example.shop.repository.UserRepository;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Business rules:
 * - username: 3-50 ky tu, khong de trong, khong trung
 * - password: toi thieu 6 ky tu
 * - email: phai co dang a@b.c
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String username, String password, String email) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username khong duoc de trong");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException("Username phai tu 3 den 50 ky tu");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password phai co it nhat 6 ky tu");
        }
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Email khong hop le");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username da ton tai");
        }
        return userRepository.save(new User(username, encoder.encode(password), email));
    }

    public Optional<User> authenticate(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        return userRepository.findByUsername(username)
                .filter(u -> encoder.matches(password, u.getPasswordHash()));
    }
}
