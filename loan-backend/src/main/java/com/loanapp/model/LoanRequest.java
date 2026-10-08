package com.loanapp.model;

import jakarta.validation.constraints.*;

public record LoanRequest(
        @NotBlank(message = "Name is required") String name,
        @DecimalMin(value = "5000", message = "Monthly income must be at least 5000") double income,
        @DecimalMin(value = "50000", message = "Loan amount must be at least 50000") double amount,
        @Min(value = 1, message = "Duration must be 1 to 30 years") @Max(value = 30, message = "Duration must be 1 to 30 years") int years,
        @NotBlank(message = "Employment is required") String employment,
        @NotBlank(message = "Credit history is required") String history) {
}
