# Graduation Thesis Management System | 毕业论文管理系统

> 哈尔滨金融学院毕业论文管理系统 — 基于 Spring Boot 3 + Spring Cloud 微服务架构，覆盖选题、指导、文档管理、查重、答辩全流程。

A microservices-based graduation thesis management platform for universities, built with Spring Boot 3.2 + Spring Cloud 2023, covering topic selection, advisor guidance, document management, similarity checking, and defense coordination.

## Tech Stack | 技术栈

### Backend

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Runtime |
| Spring Boot | 3.2.0 | Application framework |
| Spring Cloud | 2023.0.0 | Microservices stack |
| Spring Security + JWT | — | Authentication & authorization |
| MyBatis-Plus | 3.5.x | ORM |
| MySQL | 8.0+ | Primary database |
| Spring Cloud Gateway | — | API gateway |

### Frontend

| Technology | Purpose |
|---|---|
| Vue 3 | Frontend framework |
| Element Plus | UI component library |
| Pinia | State management |
| Vue Router | Client-side routing |
| ECharts | Data visualization |
| Axios | HTTP client |
| pdfjs-dist | PDF preview |

## Module Architecture | 模块架构

```text
bishedemo/                    # Parent POM (Maven multi-module)
├── bishedemo-common          # Shared utilities, constants, DTOs
├── bishedemo-system          # User / role / permission management
├── bishedemo-thesis          # Core thesis business (topics, submissions, reviews)
├── bishedemo-document        # Document upload, storage, preview
├── bishedemo-similarity      # Thesis similarity / plagiarism checking
├── bishedemo-notification    # Email & in-app notifications
├── bishedemo-gateway         # Spring Cloud Gateway (routing, rate limit)
├── hfuu-thesis-backend/      # Auth service (login, JWT issuance)
└── hfuu-thesis-frontend/     # Vue 3 single-page application
```

## Core Features | 核心功能

- **User & Role Management** — Multi-role system for students, advisors, and administrators
- **Topic Selection** — Faculty publishes topics, students select; auto-matching and manual assignment
- **Thesis Lifecycle** — Draft submission, advisor review, revision tracking, final submission
- **Document Management** — Multi-format upload (Word/PDF), online preview via pdf.js
- **Similarity Checking** — Text similarity detection module with configurable thresholds
- **Notification System** — Email and in-app notifications for key deadlines and status changes
- **Defense Management** — Defense scheduling, committee assignment, result recording
- **Dashboard** — Statistics visualization with ECharts (submission rates, review progress)

## Directory Structure | 目录结构

```text
Graduation-Thesis-Management-System/
├── pom.xml                          # Parent POM
├── bishedemo-common/
├── bishedemo-system/
├── bishedemo-thesis/
├── bishedemo-document/
├── bishedemo-similarity/
├── bishedemo-notification/
├── bishedemo-gateway/
├── hfuu-thesis-backend/
│   ├── pom.xml
│   └── src/main/java/com/hfuu/
│       ├── config/
│       ├── controller/
│       ├── entity/
│       ├── mapper/
│       └── service/
└── hfuu-thesis-frontend/
    ├── package.json
    ├── vite.config.js
    ├── src/
    │   ├── api/
    │   ├── router/
    │   ├── stores/
    │   └── views/
    └── public/
```

## Quick Start | 快速开始

### Prerequisites

- JDK 17+
- Maven 3.9+
- Node.js 18+
- MySQL 8.0+

### 1. Backend

```bash
# Clone
git clone https://github.com/coniern/Graduation-Thesis-Management-System.git
cd Graduation-Thesis-Management-System

# Configure database
# Edit src/main/resources/application.yml with your MySQL credentials

# Build all modules
mvn clean install -DskipTests

# Start gateway
cd bishedemo-gateway && mvn spring-boot:run

# Start auth service
cd hfuu-thesis-backend && mvn spring-boot:run

# Start business services as needed
cd bishedemo-system && mvn spring-boot:run
cd bishedemo-thesis && mvn spring-boot:run
```

### 2. Frontend

```bash
cd hfuu-thesis-frontend
npm install
npm run dev
```

Frontend will be available at `http://localhost:5173`.

## Default Account | 默认账号

| Role | Username | Password |
|---|---|---|
| Admin | admin | 123456 |
| Teacher | teacher | 123456 |
| Student | student | 123456 |

## API Documentation

Swagger UI is available at each service's `/swagger-ui/index.html` when running.

## Author

**Zhang Ya** — GitHub: [@coniern](https://github.com/coniern)

## License

MIT
