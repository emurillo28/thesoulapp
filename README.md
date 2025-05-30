# Soul App Backend

A **portfolio-ready, clean-architecture Spring Boot backend** for a modern mental health platform. This project is designed to demonstrate best practices in backend engineering, security, cloud deployment, and scalable architecture. It is production-oriented, fully documented, and ready for AWS deployment.

---

## 🚀 Project Overview
- **Purpose:** Provide a robust, secure, and extensible backend for a mental health platform, supporting user management, assessments, calendar events, real-time chat, and payments.
- **Audience:** Recruiters, technical reviewers, and anyone interested in modern Java backend development.
- **Cloud Native:** Built for AWS Elastic Beanstalk, RDS PostgreSQL, and Stripe payments, with CI/CD via GitHub Actions.

---

## 🏗️ Architecture & Tech Stack
- **Java 21** & **Spring Boot 3**
- **Clean Architecture:**
  - `domain`: Core business entities (User, Assessment, CalendarEvent, Message, Transaction)
  - `application`: Service interfaces and use cases
  - `infrastructure`: Persistence, security, Stripe, WebSocket
  - `web`: REST and WebSocket controllers
- **Persistence:** Spring Data JPA, PostgreSQL (RDS-ready)
- **Security:** Spring Security, JWT (stateless, role-based)
- **API Docs:** Swagger/OpenAPI (springdoc)
- **DevOps:**
  - AWS Elastic Beanstalk for deployment
  - AWS RDS PostgreSQL for database
  - GitHub Actions for CI/CD
  - Environment variables for secrets/config
- **Other:**
  - Stripe Java SDK for payments
  - Lombok for boilerplate reduction
  - WebSocket (STOMP) for real-time chat

---

## 🧩 Key Features & Modules
- **User Management & Auth:**
  - Registration, login, JWT-based authentication
  - Roles: PATIENT, THERAPIST, ADMIN
  - BCrypt password hashing
- **Assessment Management:**
  - Therapists create/approve assessments
  - Questions stored as JSONB
- **Calendar System:**
  - CRUD for events (appointments, availability, emotion tracking, personal entries)
  - Therapists set availability, patients book appointments
- **Real-Time Chat:**
  - WebSocket (STOMP) with JWT-secured handshake
  - Unique chat rooms, persisted message history
- **Payments:**
  - Stripe integration for patient payments
  - Commission logic, transaction logging
  - Secure endpoints for therapists/admins
- **API Documentation:**
  - Interactive Swagger UI at `/swagger-ui.html`

---

## 🔒 Security & Best Practices
- **JWT Authentication:** Stateless, secure, role-based access
- **Password Hashing:** BCrypt
- **Role Checks:** Enforced at controller/service level
- **Environment Variables:** All secrets (DB, JWT, Stripe) are externalized
- **CI/CD:** Automated build, test, and deploy pipeline
- **CORS:** Configurable for frontend integration

---

## ☁️ AWS, CI/CD, & DevOps
- **Elastic Beanstalk:**
  - Deploys JAR artifact
  - Environment variables for DB, JWT, Stripe
- **RDS PostgreSQL:**
  - Production DB, connection via env vars
- **GitHub Actions:**
  - On push to `main`: build, test, deploy
  - Uses AWS credentials from GitHub Secrets
- **Stripe:**
  - API key via env var
  - PaymentIntent for secure, PCI-compliant payments

---

## ⚖️ Design Decisions & Trade-Offs
- **Clean Architecture:**
  - + Testability, separation of concerns, scalability
  - - More boilerplate, more files
- **JWT (stateless):**
  - + Scalable, no session storage
  - - Requires careful token invalidation on password change
- **WebSocket with JWT:**
  - + Secure, real-time, scalable
  - - More complex handshake/auth logic
- **Stripe direct charge:**
  - + Simpler integration
  - - For production, consider Stripe Connect for payouts
- **Lombok:**
  - + Less boilerplate
  - - IDE/plugin dependency
- **Swagger/OpenAPI:**
  - + Easy API exploration
  - - Requires maintenance as API evolves

---

## ✅ Review/Study Checklist
- [ ] Understand clean architecture layering and package structure
- [ ] Review JWT authentication and role-based security
- [ ] Explore assessment and calendar event domain logic
- [ ] Test WebSocket chat with JWT handshake
- [ ] Review Stripe payment flow and commission logic
- [ ] Inspect CI/CD workflow and AWS deployment setup
- [ ] Check Swagger UI for API contract
- [ ] Review environment variable usage for secrets
- [ ] Consider test coverage and integration test examples

---

## 📝 Areas for Improvement / TODOs
- [ ] Add more integration/unit tests for all modules
- [ ] Implement Stripe Connect for therapist payouts
- [ ] Add Stripe webhook endpoint for payment status updates
- [ ] Add user profile update and password reset endpoints
- [ ] Add admin dashboard endpoints (analytics, user management)
- [ ] Add notification system (email/SMS)
- [ ] Add rate limiting and brute-force protection
- [ ] Add audit logging for sensitive actions
- [ ] Improve error handling and validation
- [ ] Add support for multi-language and time zones
- [ ] Enhance API versioning and documentation

---

## 🙏 Thanks for reviewing!
This project is designed to be a **showcase of modern backend engineering**. Please use the checklist above to guide your review and note any areas for further study or questions. PRs, issues, and feedback are welcome! 