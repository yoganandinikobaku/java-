# IssueFlow

IssueFlow is an issue-tracking application for recording software defects and following their progress from open to resolved. It provides a server-rendered web interface and a JSON REST API, backed by MySQL.

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

## Technology stack

- Java 17
- Spring Boot, Spring MVC, Spring Data JPA, Hibernate
- Thymeleaf, HTML, and CSS
- MySQL
- Jakarta Bean Validation
- Maven
- JUnit and Mockito

## Requirements

- JDK 17 or newer
- Docker Desktop with Docker Compose, or MySQL 8
- Maven, or an IDE that can import and run a Maven project

## Run locally

### Start the database

From the repository root, start the MySQL service:

```powershell
docker compose up -d
```

The Compose configuration creates the `issueflow` database with local development credentials. These credentials are for local development only.

### Start the application

```powershell
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080) in a browser.

To connect to another MySQL instance, configure the `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` environment variables before starting the application.

### Run tests

```powershell
mvn test
```

## Application structure

- **Web layer:** Spring MVC controllers and Thymeleaf templates render the issue board and forms.
- **Service layer:** `IssueService` implements issue creation, updates, filtering, and deletion.
- **Persistence layer:** `IssueRepository` uses Spring Data JPA to store issue entities in MySQL.
- **REST API:** `IssueRestController` exposes JSON endpoints for issue management.

## Issue fields

Each issue has a title, description, reporter, optional assignee, priority, status, and creation/update timestamps. Status values are `OPEN`, `IN_PROGRESS`, and `RESOLVED`; priority values are `LOW`, `MEDIUM`, and `HIGH`.

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

## Database schema

Hibernate manages the schema using `spring.jpa.hibernate.ddl-auto=update`, which is convenient for local development. For deployments that require controlled schema changes, use a migration tool such as Flyway or Liquibase.
