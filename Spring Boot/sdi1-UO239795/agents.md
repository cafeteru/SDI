# Current Project Status (sdi1-UO239795)

## Context
This is a Spring Boot project developed by Iván González Mahagamage for the deliverable 1 of the SDI subject.

## Current Tech Stack
- **Language:** Java 1.8
- **Main Framework:** Spring Boot 1.5.10.RELEASE
- **Dependency Manager:** Maven
- **Database:** H2 Database (In-memory)
- **Persistence Layer:** Spring Data JPA
- **Presentation Layer:** Thymeleaf (with Spring Security integration)
- **Security:** Spring Security
- **Others:** Spring Data REST HAL Browser, SLF4J (Logging)

## Structure
Traditional web application (MVC) rendered on the server using Thymeleaf templates, utilizing an in-memory relational database (H2) configured for fast development and testing.

## Potential Areas of Improvement (Next Steps)
1. **Version Updates:**
   - Upgrade Spring Boot version (1.5.x is deprecated). Migrate to Spring Boot 2.x or 3.x.
   - Update Java to a more recent LTS version (e.g., Java 17 or 21).
2. **Code Refactoring:**
   - Review the package structure within `src/`.
   - Apply design patterns and best practices if necessary.
3. **Tests:**
   - Ensure test coverage with `spring-boot-starter-test` (JUnit, Mockito).
4. **Security:**
   - Review WebSecurity configurations to ensure endpoints are properly protected.

---
*This document serves as a knowledge base for AI agents assisting in the improvement and maintenance of this project.*
