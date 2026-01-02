package com.primaelectronic.pos.service;

import com.primaelectronic.pos.model.User;
import com.primaelectronic.pos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> authenticate(String username, String password) {
        System.out.println("Login attempt for: " + username);
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("User found: " + user.getUsername());
            if (user.getPassword().equals(password)) {
                System.out.println("Password match!");
                return Optional.of(user);
            } else {
                System.out.println("Password mismatch.");
            }
        } else {
            System.out.println("User not found");
        }
        return Optional.empty();
    }
}
