package com.loanapp.model;

public record AuthRequest(String username, String password, String fullName) {
}
