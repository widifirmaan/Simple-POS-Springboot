package com.primaelectronic.pos.service;

import com.primaelectronic.pos.model.User;
import java.util.Optional;

public interface AuthService {
    Optional<User> authenticate(String username, String password);
}
