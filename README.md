# Graduation Thesis Management System

A multi-module graduation thesis management system for universities, designed to support topic selection, teacher guidance, document management, evaluation workflows, and administrative coordination.

## Project Overview

This repository is a larger, service-oriented thesis management project that appears to split common business capabilities into multiple modules:

- system services
- document services
- notification services
- similarity / plagiarism-related services
- gateway and common modules
- frontend and backend application modules

Compared with a single-project management system, this repository reflects a more modular system design direction.

## Repository Structure

```text
bishedemo-common/
bishedemo-document/
bishedemo-gateway/
bishedemo-notification/
bishedemo-similarity/
bishedemo-system/
bishedemo-thesis/
hfuu-thesis-backend/
hfuu-thesis-frontend/
pom.xml
```

## Possible Functional Scope

- user / role management
- topic publishing and selection
- teacher-student coordination
- thesis process tracking
- document upload and management
- notification and workflow support
- similarity-related processing

## Project Value

This repository is valuable as a portfolio project because it shows:

- awareness of modular backend design
- separation of system-level concerns
- more complex project organization than a simple CRUD app
- the ability to participate in larger education-domain systems

## Suggested Next Steps

- add a real architecture diagram
- document the role of each module
- provide startup instructions for backend and frontend
- add API and deployment notes
- include screenshots of the management interfaces
