# Ospan Ramazan Student API

Spring Boot CRUD application for the DevOps midterm (Linux, Git, Docker, Jenkins).

- **Student:** Ramazan Ospan, group IT2-2310, student ID 38045
- **Stack:** Java 21, Spring Boot 4, Gradle, PostgreSQL 16, Docker Compose, Jenkins
- **Base URL:** `http://localhost:8080/api/ospan-ramazan/students`

Status: project skeleton is ready.

## Change by Ramazan Ospan: update operation

PUT /api/ospan-ramazan/students/{id} replaces the name, e-mail and group of a student.
It returns 200 with the updated student, or 404 if the id does not exist.
