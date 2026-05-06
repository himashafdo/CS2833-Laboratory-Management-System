# Laboratory Management System

### CS2833 Modular Software Development — Company A

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)
![Java](https://img.shields.io/badge/Java-17+-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![JWT](https://img.shields.io/badge/Auth-JWT-red)

A centralized web-based Lab and Equipment Allocation System for the Department of Electronics and Telecommunications Engineering, University of Moratuwa.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Branch Strategy](#branch-strategy)
- [Team Structure](#team-structure)
- [Deadlines](#deadlines)

---

## Overview

The system digitizes and automates the department's manual resource allocation workflow — replacing physical logbooks and verbal communication with a centralized digital platform.

**Key Features:**

- Secure JWT-based authentication with role-based access control
- Live equipment and lab catalog with real-time availability
- Online reservation system with conflict detection
- Issue reporting and ticket management
- Admin dashboard with full system oversight
- Equipment request and procurement management
- Demand monitoring and analytics

---

## Tech Stack

| Layer      | Technology                      |
| ---------- | ------------------------------- |
| Language   | Java 17+                        |
| Framework  | Spring Boot 3.2.5               |
| Security   | Spring Security + JWT           |
| ORM        | Spring Data JPA + Hibernate     |
| Database   | MySQL 8.0                       |
| Frontend   | HTML5, CSS3, Vanilla JavaScript |
| Build Tool | Maven                           |
| Deployment | Railway                         |

---

## Project Structure

    src/
    ├── main/
    │   ├── java/com/companya/labms/
    │   │   ├── auth/              # Authentication module
    │   │   ├── catalog/           # Equipment and lab catalog
    │   │   ├── reservation/       # Booking and reservation
    │   │   ├── ticketing/         # Issue reporting
    │   │   ├── request/           # Equipment requests
    │   │   └── shared/            # BaseEntity, Role, EmailService
    │   └── resources/
    │       ├── static/            # Frontend HTML/CSS/JS files
    │       └── application.properties

---

## Getting Started

### Prerequisites

- Java 17 or higher
- MySQL 8.0
- Maven
- Git

### 1 — Clone the Repository

    git clone https://github.com/himashafdo/CS2833-Laboratory-Management-System.git
    cd CS2833-Laboratory-Management-System

### 2 — Create the Database

Open MySQL Workbench and run:

    CREATE DATABASE labms;

### 3 — Configure application.properties

Open `src/main/resources/application.properties` and set:

    spring.datasource.url=jdbc:mysql://localhost:3306/labms
    spring.datasource.username=root
    spring.datasource.password=YOUR_MYSQL_PASSWORD
    spring.jpa.hibernate.ddl-auto=update
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=YOUR_GMAIL
    spring.mail.password=YOUR_GMAIL_APP_PASSWORD

### 4 — Run the Application

**Windows:**

    ./mvnw spring-boot:run

**Mac:**

    chmod +x mvnw
    bash mvnw spring-boot:run

### 5 — Access the Application

Open your browser and go to:

    http://localhost:8080

### 6 — Insert Sample Data

After the server starts, open MySQL Workbench and run:

    USE labms;

    INSERT INTO equipment (created_at, updated_at, name, description, image_url, status, quantity)
    VALUES
    (NOW(), NOW(), 'Oscilloscope DSO-X 1204G', 'Digital Oscilloscope', '', 'AVAILABLE', 50),
    (NOW(), NOW(), 'DC Power Supply 30V/5A', 'DC Power Supply', '', 'AVAILABLE', 60),
    (NOW(), NOW(), 'AC Power Supply', 'AC Power Supply', '', 'AVAILABLE', 40),
    (NOW(), NOW(), 'Digital Multimeter', 'Digital Multimeter', '', 'AVAILABLE', 60),
    (NOW(), NOW(), 'Analog Multimeter', 'Analog Multimeter', '', 'AVAILABLE', 60),
    (NOW(), NOW(), 'DE0-Nano FPGA Board', 'FPGA Development Board', '', 'AVAILABLE', 10),
    (NOW(), NOW(), 'USRP B210 SDR Kit', 'Software Defined Radio Kit', '', 'AVAILABLE', 10),
    (NOW(), NOW(), 'Raspberry Pi 4 Kit', 'Raspberry Pi 4 with accessories', '', 'AVAILABLE', 15);

    INSERT INTO labs (created_at, updated_at, lab_name, location, status)
    VALUES
    (NOW(), NOW(), 'Telecom Lab', '2nd Floor, Building ENTC', 'AVAILABLE'),
    (NOW(), NOW(), 'Digital Lab', '1st Floor, Building ENTC', 'AVAILABLE'),
    (NOW(), NOW(), 'Analog Lab', '1st Floor, Building ENTC', 'AVAILABLE'),
    (NOW(), NOW(), 'Computer Lab', '3rd Floor, Building ENTC', 'AVAILABLE');

---

## API Endpoints

| Module      | Base Path         |
| ----------- | ----------------- |
| Auth        | /api/auth         |
| Catalog     | /api/catalog      |
| Reservation | /api/reservations |
| Issues      | /api/issues       |
| Requests    | /api/requests     |

Full API documentation is available in the Wiki.

---

## Branch Strategy

| Branch                   | Purpose                                   |
| ------------------------ | ----------------------------------------- |
| main                     | Production ready code — merge only via PR |
| feature/auth             | Authentication module                     |
| feature/catalog          | Catalog module                            |
| feature/reservation      | Reservation module                        |
| feature/ticketing        | Issue ticketing module                    |
| feature/studentdashboard | Student frontend pages                    |
| feature/admindashboard   | Admin frontend pages                      |
| feature/advancedmodules  | Equipment request and analytics           |

**Rules:**

- Never push directly to main
- Always create a PR for merging
- Pull latest main before starting work
- One module per branch

---

## Team Structure

| Group                  | Module                                 | Branch                  |
| ---------------------- | -------------------------------------- | ----------------------- |
| Company Head (Himasha) | Auth + Core Backend + Student Frontend | feature/auth            |
| Group 1                | Catalog Management                     | feature/admindashboard  |
| Group 2 and 3          | Equipment Request Module               | feature/advancedmodules |
| Group 4                | Issue Ticketing Management             | feature/admindashboard  |
| Group 5                | Dashboard + User Management            | feature/admindashboard  |
| Group 6                | Procurement                            | feature/admindashboard  |
| Group 7                | Demand Monitoring and Analytics        | feature/advancedmodules |
| Group 8                | Reservation Management                 | feature/admindashboard  |

---

## Deadlines

| Milestone          | Date            |
| ------------------ | --------------- |
| Backend Deadline   | May 12, 2026    |
| Frontend Deadline  | May 23, 2026    |
| UAT with Company B | May 8, 2026     |
| Final Demo         | May 15-26, 2026 |

---

## Important Notes for All Team Members

1. Never commit application.properties with real passwords
2. Always run git pull origin main before starting work
3. Always test your endpoints in Postman before pushing
4. Use the issue assigned to you and close it in your PR with Closes #issue-number
5. Ask the Company Head before merging to main

---

Made with coffee by Company A — CS2833 MSD 2026
