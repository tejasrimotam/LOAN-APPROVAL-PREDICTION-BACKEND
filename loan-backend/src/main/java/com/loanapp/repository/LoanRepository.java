package com.loanapp.repository;

import com.loanapp.model.LoanApplication;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findAllByOrderByIdDesc();
}
