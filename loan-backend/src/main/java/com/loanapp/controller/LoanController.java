package com.loanapp.controller;

import com.loanapp.model.LoanApplication;
import com.loanapp.model.LoanRequest;
import com.loanapp.repository.LoanRepository;
import com.loanapp.service.AuthService;
import com.loanapp.service.EligibilityService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*", "https://*.vercel.app"})
public class LoanController {

    private final LoanRepository repo;
    private final EligibilityService eligibility;
    private final AuthService auth;

    public LoanController(LoanRepository repo, EligibilityService eligibility, AuthService auth) {
        this.repo = repo;
        this.eligibility = eligibility;
        this.auth = auth;
    }

    /** Admin only. */
    @GetMapping
    public List<LoanApplication> all(@RequestHeader(value = "Authorization", required = false) String h) {
        auth.require(h, "ADMIN");
        return repo.findAllByOrderByIdDesc();
    }

    /** Admin only. */
    @GetMapping("/{id}")
    public ResponseEntity<LoanApplication> one(@PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String h) {
        auth.require(h, "ADMIN");
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /** Any logged-in user (admin or applicant). */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanApplication create(@Valid @RequestBody LoanRequest req,
            @RequestHeader(value = "Authorization", required = false) String h) {
        var user = auth.require(h, "ADMIN", "APPLICANT");
        var result = eligibility.evaluate(req);
        var app = new LoanApplication();
        app.setName(req.name().trim());
        app.setIncome(req.income());
        app.setAmount(req.amount());
        app.setYears(req.years());
        app.setEmployment(req.employment());
        app.setHistory(req.history());
        app.setScore(result.score());
        app.setEmi(Math.round(result.emi()));
        app.setStatus(result.approved() ? "Approved" : "Not Approved");
        app.setSubmittedBy(user.getUsername());
        return repo.save(app);
    }
}
