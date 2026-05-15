<<<<<<< HEAD
# Lab Management System — Local Setup Guide
**MSD | Company A**
=======
# Laboratory Management System
>>>>>>> origin/main

### CS2833 Modular Software Development — Company A

<<<<<<< HEAD
---

## STEP 1 — INSTALL VS CODE
=======
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)
![Java](https://img.shields.io/badge/Java-17+-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![JWT](https://img.shields.io/badge/Auth-JWT-red)
>>>>>>> origin/main

A centralized web-based Lab and Equipment Allocation System for the Department of Electronics and Telecommunications Engineering, University of Moratuwa.

<<<<<<< HEAD
Install these VS Code extensions (press Ctrl+Shift+X):
- **Extension Pack for Java** (by Microsoft)
- **Spring Boot Extension Pack** (by VMware)
- **GitLens** (by GitKraken)

Wait for extensions to finish installing. Reload if asked.

---

## STEP 2 — INSTALL JAVA (JDK 17+)

> If you already have JDK 17, 21, or 25 installed — skip this step.

1. Go to: https://adoptium.net/temurin/releases/
2. Select: Version 17, OS: Windows, Arch: x64, Type: JDK
3. Download the .msi installer
4. Run it — tick these on the install screen:
   - [x] Set JAVA_HOME variable
   - [x] Add to PATH
5. Click Next and finish

Verify — open Command Prompt and type:
```
java -version
```
You should see: `openjdk version "17.x.x"`
If you see an error, restart your computer and try again.

---

## STEP 3 — INSTALL MYSQL

1. Go to: https://dev.mysql.com/downloads/installer/
2. Download "MySQL Installer for Windows" (~450MB)
3. Run the installer → choose **Developer Default**
4. Keep clicking Next / Execute until everything installs
5. When asked for a root password:
   - **Leave it BLANK** (just click Next) OR write it down
6. Finish installation (MySQL Workbench is included)

---

## STEP 4 — CREATE THE DATABASE

1. Open **MySQL Workbench**
2. Click the connection box (Local instance MySQL80)
3. Enter your password if set, or press Enter if blank
4. In the query area type:
```sql
CREATE DATABASE labms;
```
5. Press **Ctrl+Enter** — you should see "1 row affected"

---

## STEP 5 — INSTALL GIT

1. Go to: https://git-scm.com/download/win
2. Download and run the installer (click Next on everything)

Verify:
```
git --version
```

---

## STEP 6 — CLONE THE PROJECT

In VS Code press **Ctrl+Shift+P** → type **Git Clone** → paste:
```
https://github.com/himashafdo/CS2833-Laboratory-Management-System.git
```
Choose Desktop as the folder. Click Open when asked.

---

## STEP 7 — SWITCH TO YOUR TEAM'S BRANCH

Open the VS Code terminal (Ctrl + `) and run YOUR branch:

| Team | Branch |
|------|--------|
| Team 8 (Auth) | `git checkout feature/auth` |
| Team 2 (Catalog) | `git checkout feature/catalog` |
| Team 6 (Reservation) | `git checkout feature/reservation` |
| Teams 3, 4 (Ticketing) | `git checkout feature/ticketing` |
| Teams 4, 5 (Admin Dashboard) | `git checkout feature/admindashboard` |
| Team 7 (Advanced Modules) | `git checkout feature/advancedmodules` |
| Team 1 (Student Dashboard) | `git checkout feature/studentdashboard` |

> ⚠️ Only checkout YOUR branch. DO NOT work on main!

---

## STEP 8 — CONFIGURE DATABASE PASSWORD

Open `src/main/resources/application.properties` and find:
```
spring.datasource.password=root
```
Change it to match your MySQL password. If you left it blank:
```
spring.datasource.password=
```
Save the file (Ctrl+S).

---

## STEP 9 — RUN THE PROJECT

In the VS Code terminal run:
```
./mvnw clean spring-boot:run
```
First time takes 2-3 minutes. Wait until you see:
```
Started LabmsApplication in x.xxx seconds
```
Open your browser and go to: **http://localhost:8080**

You should see the login page. ✅

---

## STEP 10 — LOAD SAMPLE DATA

Once the project is running, open **MySQL Workbench** and run this SQL to load the sample equipment and labs:
```sql
USE labms;

-- Equipment
INSERT INTO equipment (created_at, updated_at, name, description, image_url, status, quantity) VALUES
(NOW(), NOW(), 'Oscilloscope DSO-X 1204G', 'Test & Measurement — 4 channel 200MHz digital oscilloscope', '', 'AVAILABLE', 50),
(NOW(), NOW(), 'DC Power Supply 30V/5A', 'Power Equipment — Variable DC bench power supply', '', 'AVAILABLE', 60),
(NOW(), NOW(), 'AC Power Supply', 'Power Equipment — Programmable AC power source', '', 'AVAILABLE', 40),
(NOW(), NOW(), 'Digital Multimeter', 'Test & Measurement — Fluke 87V digital multimeter', '', 'AVAILABLE', 60),
(NOW(), NOW(), 'Analog Multimeter', 'Test & Measurement — Analog panel meter', '', 'AVAILABLE', 60),
(NOW(), NOW(), 'DE0-Nano FPGA Board', 'Digital Design — Altera Cyclone IV FPGA development board', '', 'AVAILABLE', 10),
(NOW(), NOW(), 'USRP B210 SDR Kit', 'Software-Defined Radio — Full duplex SDR from DC to 6GHz', '', 'AVAILABLE', 10),
(NOW(), NOW(), 'Raspberry Pi 4 Kit', 'Embedded Systems — RPi 4 with 4GB RAM accessories included', '', 'AVAILABLE', 15);

-- Labs
INSERT INTO labs (created_at, updated_at, lab_name, location, status) VALUES
(NOW(), NOW(), 'Telecom Lab', '3rd Floor, Building ENTC', 'AVAILABLE'),
(NOW(), NOW(), 'Digital Lab', '2nd Floor, Building ENTC', 'AVAILABLE'),
(NOW(), NOW(), 'Analog Lab', '2nd Floor, Building ENTC', 'AVAILABLE'),
(NOW(), NOW(), 'Computer Lab', '1st Floor, Building ENTC', 'AVAILABLE');
```

Then run this to check IDs and map equipment to labs:
```sql
SELECT id, name FROM labms.equipment;
SELECT id, lab_name FROM labms.labs;
```

Then insert lab-equipment mappings using the IDs from above.
Ask the team lead for the exact mapping SQL.

---

## STEP 11 — START WORKING ON YOUR MODULE

Your module folder:
```
src/main/java/com/companya/labms/YOUR_MODULE_NAME/
```

Each team must build:
1. `Entity.java` — database table (extends BaseEntity)
2. `Repository.java` — talks to the database
3. `Service.java` — business logic
4. `Controller.java` — REST API endpoints
5. An HTML page in `src/main/resources/static/`

After changes, commit to YOUR branch only:
```
git add .
git commit -m "describe what you did"
git push origin feature/YOUR_MODULE_NAME
```
> ❌ DO NOT push to main. The team lead (Himasha) will merge.

---

## IMPORTANT RULES

- Always pull before starting work:
```
  git pull origin feature/YOUR_MODULE_NAME
```
- Never commit directly to main ❌
- For testing API endpoints use Postman with:
```
  Header: Authorization: Bearer YOUR_JWT_TOKEN
```
- If project won't start check:
  1. Is MySQL running? (search Services in Windows → MySQL80 → Running)
  2. Is the database created? (Step 4)
  3. Is your password correct in application.properties?

- If you get JAVA_HOME error run in PowerShell (Admin):
```
  [System.Environment]::SetEnvironmentVariable("JAVA_HOME","C:\Program Files\Eclipse Adoptium\jdk-17.0.14.7-hotspot","Machine")
```
  Then restart VS Code.

---

## CONTACT

If stuck — message the team lead before wasting time.
Do NOT guess and break things on main branch.

**GitHub Repo:**
https://github.com/himashafdo/CS2833-Laboratory-Management-System.git
=======
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
>>>>>>> origin/main
