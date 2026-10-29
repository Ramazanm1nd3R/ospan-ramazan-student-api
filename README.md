# Ospan Ramazan Student API

Spring Boot CRUD application for the DevOps midterm (Linux, Git, Docker, Jenkins).

- **Student:** Ramazan Ospan, group IT2-2310, student ID 38045
- **Stack:** Java 21, Spring Boot 4, Gradle, PostgreSQL 16, Docker Compose, Jenkins
- **Base URL:** `http://localhost:8080/api/ospan-ramazan/students`

Status: CRUD API (create, read, update, delete) is ready on main; documentation reviewed by Ramazan Ospan.

## Change by Ramazan Ospan: update operation

PUT /api/ospan-ramazan/students/{id} replaces the name, e-mail and group of a student.
It returns 200 with the updated student, or 404 if the id does not exist.

## Run with Docker Compose

```bash
cp .env.example .env          # set your own DB_PASSWORD; .env is never committed
docker compose up -d --build  # PostgreSQL + application, image ospan-ramazan-student-api:v1.1-ospan-ramazan
curl http://localhost:8080/   # shows the student name and the application version
```

| Method | URL | Result |
|---|---|---|
| POST | `/api/ospan-ramazan/students` | create a student (201) |
| GET | `/api/ospan-ramazan/students` | list all students |
| GET | `/api/ospan-ramazan/students/{id}` | one student, 404 if it does not exist |
| PUT | `/api/ospan-ramazan/students/{id}` | update a student |
| DELETE | `/api/ospan-ramazan/students/{id}` | delete a student (204) |

## CI/CD

`Jenkinsfile` runs Checkout → Build → Test → Docker Build → Verify on Jenkins job `Ospan_Ramazan_Student_API`.
The Jenkins controller itself is described in `ci/jenkins` (plugins and configuration as code).

Jenkins polls GitHub every 2 minutes (`pollSCM`), so every push to `main` or a `test/*` branch starts the pipeline automatically.
Failed builds stop at the failing stage: no Docker image is built from code whose tests fail.
