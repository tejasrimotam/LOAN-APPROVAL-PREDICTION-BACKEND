package com.loanapp.service;

import com.loanapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Creates the demo accounts the first time the app runs. */
@Component
public class DataSeeder implements CommandLineRunner {

    private final AuthService auth;
    private final UserRepository users;

    public DataSeeder(AuthService auth, UserRepository users) {
        this.auth = auth;
        this.users = users;
    }

    @Override
    public void run(String... args) {
        if (users.count() == 0) {
            auth.createUser("admin", "admin123", "Loan Officer", "ADMIN");
            auth.createUser("tejasri", "user123", "Tejasri", "APPLICANT");
        }
    }
}
