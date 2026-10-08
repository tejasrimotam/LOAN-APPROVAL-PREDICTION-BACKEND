# Loan Approval Prediction - Spring Boot Backend

Needs: JDK 17+, MySQL (or the h2 profile).

1. Set your MySQL password in src/main/resources/application.properties
2. Run LoanApprovalApplication (VS Code: Run link above main) or: mvn spring-boot:run

Demo logins (created automatically on first run):
- admin / admin123     (ADMIN: sees everything)
- tejasri / user123    (APPLICANT: can submit applications)

API
- POST /api/auth/login, POST /api/auth/register
- POST /api/applications   (any logged-in user)
- GET  /api/applications   (ADMIN only)
