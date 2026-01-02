package com.primaelectronic.pos.controller;

import com.primaelectronic.pos.model.User;
import com.primaelectronic.pos.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session,
            Model model) {
        System.out.println("Login attempt for: " + username);
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("User found: " + user.getUsername());
            // In a real app, passwords should be hashed. The PHP app seemed to store plain
            // text or didn't specify hashing in the snippet I saw.
            // Assuming plain text for direct migration compatibility unless I see
            // password_verify in PHP.
            // The PHP snippet showed: $sql = "SELECT * FROM user WHERE username='$username'
            // AND password='$password'";
            // So it's plain text comparison.

            if (user.getPassword().equals(password)) {
                System.out.println("Password match!");
                session.setAttribute("user", user);
                session.setAttribute("username", user.getUsername());
                session.setAttribute("store", user.getStore());
                return "redirect:/dashboard";
            } else {
                System.out.println("Password mismatch. Input: " + password + ", DB: " + user.getPassword());
            }
        } else {
            System.out.println("User not found");
        }

        model.addAttribute("error", "Email atau password Anda salah. Silakan coba lagi!");
        return "index"; // Return to login page with error
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
