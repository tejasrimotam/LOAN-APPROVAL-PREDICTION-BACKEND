package com.loanapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "loan_applications")
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double income;
    private double amount;
    @Column(name = "duration_years")
    private int years;
    private String employment;
    private String history;
    private int score;
    private String status;
    private double emi;
    @Column(name = "submitted_by")
    private String submittedBy;
    @Column(name = "application_date")
    private LocalDate applicationDate = LocalDate.now();

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getIncome() { return income; }
    public void setIncome(double income) { this.income = income; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public int getYears() { return years; }
    public void setYears(int years) { this.years = years; }
    public String getEmployment() { return employment; }
    public void setEmployment(String employment) { this.employment = employment; }
    public String getHistory() { return history; }
    public void setHistory(String history) { this.history = history; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getEmi() { return emi; }
    public void setEmi(double emi) { this.emi = emi; }
    public String getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(String s) { this.submittedBy = s; }
    @JsonProperty("date")
    public LocalDate getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDate d) { this.applicationDate = d; }
}
