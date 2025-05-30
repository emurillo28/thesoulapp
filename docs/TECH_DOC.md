# Technical Documentation: Soul App Backend

## Clean Architecture Structure

The project follows a clean architecture approach, separating concerns into the following layers:

- **domain**: Core business entities and logic (e.g., User, Assessment, CalendarEvent).
- **application**: Use cases and service interfaces (e.g., UserService).
- **infrastructure**: Implementations for persistence, security, and external integrations (e.g., JPA repositories, JWT provider).
- **web**: REST controllers and web configuration.

## User Management & Authentication

### User Entity
- Fields: id (UUID), email, password (hashed), role (PATIENT, THERAPIST, ADMIN), profile (JSON), createdAt (Instant)
- JPA entity with repository for CRUD and lookup by email.

### Next Steps
- Implement UserService (registration, login, profile retrieval)
- Implement UserController (signup, login, profile endpoints)
- Add JWT authentication and Spring Security 

## Progress
- User entity and repository implemented
- UserService and UserController implemented
- JWT authentication and basic Spring Security config added

## User Endpoints
- POST /auth/signup — Register a new user
- POST /auth/login — Login and receive JWT
- GET /users/me — Get current user profile (JWT required)

## Next Steps
- Add JWT filter to security config
- Use JWT for /users/me endpoint (extract email from token)
- Add test coverage 

## AWS Deployment & CI/CD

### Elastic Beanstalk (EBS)
- Deploys the Spring Boot JAR to AWS EBS.
- Environment variables (set in EBS console):
  - RDS_JDBC_URL
  - RDS_USERNAME
  - RDS_PASSWORD
  - JWT_SECRET

### RDS PostgreSQL
- Database connection is configured via environment variables in `application-prod.properties`.
- Use AWS Secrets Manager or EBS env vars for credentials.

### GitHub Actions CI/CD
- Workflow: `.github/workflows/ci-cd.yml`
- On push to `main`:
  - Build and test with Maven
  - Deploy to EBS using AWS credentials from GitHub Secrets

## Example Environment Variables
```
RDS_JDBC_URL=jdbc:postgresql://<rds-endpoint>:5432/thesoulapp
RDS_USERNAME=your_db_user
RDS_PASSWORD=your_db_password
JWT_SECRET=your_jwt_secret
```

## Assessment Management System

### Entity
- Fields: id, title, description, createdBy (therapist ID), approvedBy (list of therapist IDs), questions (JSONB), createdAt

### Endpoints
- POST /api/assessments — Create assessment (therapist only)
- GET /api/assessments — List all assessments
- POST /api/assessments/{id}/approve — Approve assessment (therapist only)

### Security
- Only users with the THERAPIST role can create or approve assessments (enforced via JWT and role checks). 

## Calendar Event System

### Entity
- Fields: id, userId, type (APPOINTMENT, PERSONAL_ENTRY, EMOTION_TRACKING, AVAILABILITY), startTime, endTime, details (JSONB), createdAt

### Endpoints
- POST /api/calendar — Create event (owner only)
- PUT /api/calendar/{id} — Update event (owner only)
- DELETE /api/calendar/{id} — Delete event (owner only)
- GET /api/calendar — List/filter events
- POST /api/calendar/availability — Therapist sets availability
- POST /api/calendar/book — Patient books appointment

### Security
- Only owners can modify their events
- Only therapists can set availability
- Only patients can book appointments 

## Real-Time Chat System

### Entity
- Fields: id, roomId, senderId, recipientId, content, timestamp

### WebSocket
- Endpoint: /ws (STOMP)
- Topic: /topic/chat/{roomId}
- Send: /app/chat.sendMessage
- JWT required for handshake

### REST Endpoint
- GET /api/chat/{roomId}/messages — Get message history (participants only)

### Security
- Only participants can send/receive messages in a room
- WebSocket connections require valid JWT 

## Stripe Payment Integration

### Entity
- Fields: id, patientId, therapistId, amount, commission, stripePaymentId, status, createdAt

### Endpoints
- POST /api/payments — Charge patient for appointment (patients only)
- GET /api/transactions — List transactions (therapists/admins only)

### Security
- Only patients can initiate payments
- Only therapists/admins can view transactions
- Stripe API key is set via environment variable