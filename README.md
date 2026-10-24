# IssueFlow

IssueFlow is a small issue-tracking web application built with Java and Spring Boot. It gives a team a visual board to create, search, assign, edit, and track software issues through their lifecycle.

## Features

- Dashboard counts for all, open, in-progress, and resolved issues
- Create, view, edit, and delete issues
- Track reporter, assignee, status, and priority
- Search issue titles and descriptions and filter by status
- Validation with helpful form errors
- MySQL persistence through Spring Data JPA and Hibernate
- Responsive Thymeleaf interface
- JSON REST API for issue CRUD operations
- Docker Compose setup for a local MySQL database
- Service unit test using JUnit and Mockito

## Technology

Java 17, Spring Boot, Spring MVC, Thymeleaf, Spring Data JPA, Hibernate, Jakarta Bean Validation, MySQL, Maven, HTML, and CSS.

## Run locally

### Prerequisites

- JDK 17 or newer
- Docker Desktop with Docker Compose, or a local MySQL 8 server
- Maven (or import the `pom.xml` into IntelliJ IDEA/Eclipse)

### Start MySQL with Docker

From the project directory:

```powershell
docker compose up -d
```

The development database is `issueflow`. The local-only credentials are configured in `docker-compose.yml` and `application.properties`. Change them before using this outside your own computer.

### Start the application

```powershell
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080).

To use a separate MySQL instance, set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` in your environment before starting the application.

## Main routes

| Method | Route | Purpose |
| --- | --- | --- |
| GET | `/issues` | Dashboard, search, and status filter |
| GET | `/issues/new` | Issue creation form |
| POST | `/issues` | Create issue |
| GET | `/issues/{id}` | Issue details |
| GET | `/issues/{id}/edit` | Edit form |
| POST | `/issues/{id}/edit` | Save changes |
| POST | `/issues/{id}/delete` | Delete issue |

### JSON REST API

| Method | Route | Purpose |
| --- | --- | --- |
| GET | `/api/issues` | List issues; optional `q` and `status` filters |
| GET | `/api/issues/{id}` | Get an issue |
| POST | `/api/issues` | Create an issue (JSON body) |
| PUT | `/api/issues/{id}` | Update an issue (JSON body) |
| DELETE | `/api/issues/{id}` | Delete an issue |

Example JSON for `POST /api/issues`:

```json
{
  "title": "Checkout page returns an error",
  "description": "Submitting a valid order returns a 500 response.",
  "reporter": "Nandini",
  "assignee": "Developer",
  "status": "OPEN",
  "priority": "HIGH"
}
```

Validation failures and missing issue IDs return JSON error responses on the API routes.

Run the tests with:

```powershell
mvn test
```

## Database note

For this learning project, Hibernate creates/updates tables using `spring.jpa.hibernate.ddl-auto=update`. For a production application, use versioned migrations such as Flyway or Liquibase instead.

## Suggested interview walkthrough

1. Explain the issue lifecycle: open, in progress, resolved.
2. Trace a form submission from `IssueController` through `IssueService` and `IssueRepository` to MySQL.
3. Explain why status and priority are enums and stored as strings.
4. Show validation in `IssueForm` and the form error messages.
5. Demonstrate search, status filtering, and the dashboard counts.
6. Discuss a future improvement, such as authentication, comments, pagination, or REST API tests.

Build and run the project yourself, make a few changes, and be ready to explain the code before presenting it as your work.
