package com.loanapp.service;

import com.loanapp.model.LoanRequest;
import org.springframework.stereotype.Service;

/** Same rules as the React evaluate() function, keep both in sync. */
@Service
public class EligibilityService {

    public static final double RATE = 10.5; // % per year

    public record Result(int score, boolean approved, double emi, double foir) {}

    public double emi(double principal, int years) {
        double r = RATE / 1200.0;
        int n = years * 12;
        return principal * r * Math.pow(1 + r, n) / (Math.pow(1 + r, n) - 1);
    }

    public Result evaluate(LoanRequest req) {
        double emi = emi(req.amount(), req.years());
        double foir = emi / req.income();

        int credit = switch (req.history()) {
            case "Good" -> 35;
            case "Average" -> 18;
            default -> 0;
        };
        int burden = foir <= 0.3 ? 30 : foir <= 0.4 ? 20 : foir <= 0.5 ? 10 : 0;
        int job = switch (req.employment()) {
            case "Salaried" -> 20;
            case "Self-employed" -> 14;
            case "Business" -> 12;
            default -> 0;
        };
        double ratio = req.amount() / (req.income() * 12);
        int size = ratio <= 3 ? 15 : ratio <= 5 ? 8 : 0;

        int score = credit + burden + job + size;
        boolean approved = score >= 60 && foir <= 0.5 && !"Unemployed".equals(req.employment());
        return new Result(score, approved, emi, foir);
    }
}
