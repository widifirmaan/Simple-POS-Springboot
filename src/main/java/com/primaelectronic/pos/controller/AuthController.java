package com.primaelectronic.pos.controller;

import com.primaelectronic.pos.model.User;
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
    private com.primaelectronic.pos.service.AuthService authService;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session,
            Model model) {

        Optional<User> userOpt = authService.authenticate(username, password);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("store", user.getStore());
            return "redirect:/dashboard";
        }

        model.addAttribute("error", "Email atau password Anda salah. Silakan coba lagi!");
        return "index";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
