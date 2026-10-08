package com.loanapp.service;

import com.loanapp.model.AppUser;
import com.loanapp.model.AuthRequest;
import com.loanapp.model.AuthResponse;
import com.loanapp.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/** Simple login: BCrypt passwords in MySQL + in-memory session tokens. */
@Service
public class AuthService {

    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final Map<String, AppUser> sessions = new ConcurrentHashMap<>();

    public AuthService(UserRepository users) {
        this.users = users;
    }

    public AppUser createUser(String username, String password, String fullName, String role) {
        var u = new AppUser();
        u.setUsername(username.trim().toLowerCase());
        u.setPasswordHash(encoder.encode(password));
        u.setFullName(fullName);
        u.setRole(role);
        return users.save(u);
    }

    public AuthResponse register(AuthRequest r) {
        if (r.fullName() == null || r.fullName().trim().length() < 2) throw bad("Enter your full name");
        if (r.username() == null || r.username().trim().length() < 3) throw bad("Username must be at least 3 characters");
        if (r.password() == null || r.password().length() < 6) throw bad("Password must be at least 6 characters");
        if (users.findByUsername(r.username().trim().toLowerCase()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already taken");
        return open(createUser(r.username(), r.password(), r.fullName().trim(), "APPLICANT"));
    }

    public AuthResponse login(AuthRequest r) {
        var user = users.findByUsername(r.username() == null ? "" : r.username().trim().toLowerCase());
        if (user.isEmpty() || r.password() == null || !encoder.matches(r.password(), user.get().getPasswordHash()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password");
        return open(user.get());
    }

    public AppUser require(String header, String... roles) {
        AppUser u = header != null && header.startsWith("Bearer ") ? sessions.get(header.substring(7)) : null;
        if (u == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please log in again");
        if (!List.of(roles).contains(u.getRole()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access");
        return u;
    }

    private AuthResponse open(AppUser u) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, u);
        return new AuthResponse(token, u.getUsername(), u.getFullName(), u.getRole());
    }

    private ResponseStatusException bad(String msg) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
    }
}
