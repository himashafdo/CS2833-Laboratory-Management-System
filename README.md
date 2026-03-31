======================================================
    LAB MANAGEMENT SYSTEM — LOCAL SETUP GUIDE
 		MSD| Company A
================================================================

Follow every step in order. Do NOT skip anything.

----------------------------------------------------------------
STEP 1 — INSTALL VS CODE
----------------------------------------------------------------

1. Go to: https://code.visualstudio.com/download
2. Download the Windows installer (.exe)
3. Run it, click Next on everything, finish install
4. Open VS Code

Install these VS Code extensions (press Ctrl+Shift+X to open extensions):
   - Extension Pack for Java   (by Microsoft)
   - Spring Boot Extension Pack (by VMware)
   - GitLens                   (by GitKraken)

Wait for extensions to finish installing. VS Code may ask you
to reload — click Reload.


----------------------------------------------------------------
STEP 2 — INSTALL JAVA (JDK 17 or a newer version is fine if youve already have a new version like 21 or 25 skip this step of later error occurs it wont be because of the version you just have to point to your jdk file)
----------------------------------------------------------------

1. Go to: https://adoptium.net/temurin/releases/
2. Select:
      Version:  17
      OS:       Windows
      Arch:     x64
      Type:     JDK
3. Download the .msi installer
4. Run it — on the install screen, make sure to tick:
      [x] Set JAVA_HOME variable
      [x] Add to PATH
5. Click Next and finish

Verify it worked — open Command Prompt (search "cmd") and type:
   java -version

You should see something like: openjdk version "17.x.x"
If you see an error, restart your computer and try again.


----------------------------------------------------------------
STEP 3 — INSTALL MYSQL
----------------------------------------------------------------

1. Go to: https://dev.mysql.com/downloads/installer/
2. Download "MySQL Installer for Windows" (the larger ~450MB one)
3. Run the installer
4. Choose "Developer Default" setup type, click Next
5. Keep clicking Next / Execute until everything installs
6. When it asks you to set a root password:
      *** IMPORTANT: leave it BLANK (just click Next) ***
      OR write down whatever password you set
7. Finish the installation

Also install MySQL Workbench if it wasn't included
(it usually is with Developer Default).


----------------------------------------------------------------
STEP 4 — CREATE THE DATABASE
----------------------------------------------------------------

1. Open MySQL Workbench
2. Click the box under "MySQL Connections" (Local instance MySQL80)
3. If it asks for a password, enter what you set in Step 3
   (or just press Enter if you left it blank)
4. In the query area at the top, type:
      CREATE DATABASE labms;
5. Press Ctrl+Enter to run it
6. You should see "1 row affected" at the bottom — done


----------------------------------------------------------------
STEP 5 — INSTALL GIT
----------------------------------------------------------------

1. Go to: https://git-scm.com/download/win
2. Download and run the installer
3. Click Next on everything (default settings are fine)
4. Finish install

Verify — open Command Prompt and type:
   git --version

You should see: git version 2.x.x


----------------------------------------------------------------
STEP 6 — CLONE THE PROJECT
----------------------------------------------------------------

1. Open VS Code
2. Press Ctrl+Shift+P
3. Type: Git Clone
4. Paste this URL:
      https://github.com/himashafdo/CS2833-Laboratory-Management-System.git
5. Choose a folder to save it (e.g. Desktop)
6. Click "Open" when VS Code asks to open the cloned repo

OR do it via Command Prompt:
   cd Desktop
   git clone https://github.com/himashafdo/CS2833-Laboratory-Management-System.git
   cd CS2833-Laboratory-Management-System


----------------------------------------------------------------
STEP 7 — SWITCH TO YOUR TEAM'S BRANCH
----------------------------------------------------------------

Open the VS Code terminal (Ctrl + `) and type the command
for YOUR module:

   Team 8 (Auth): git checkout feature/auth
   Team 2 (Catalog): git checkout feature/catalog
   Team 6 (Reservation): git checkout feature/reservation
   Team 3,4 (Ticketing):  git checkout feature/ticketing
   Team 5 (Admin Dashboard): git checkout feature/admindashboard
   Team 7 (Advanced Modules):  git checkout feature/advancedmodules
   Team 1 (Student Dashboard): git checkout feature/studentdashboard

Only checkout YOUR team's branch. Do NOT work on main.(very important) DO NOT PUSH ANYTHING TO MAIN!!!!!!!!


----------------------------------------------------------------
STEP 8 — CONFIGURE DATABASE PASSWORD
----------------------------------------------------------------

In VS Code, open this file:
   src/main/resources/application.properties

Find this line:
   spring.datasource.password=root

Change it to match what you set in Step 3.
If you left the password blank, change it to:
   spring.datasource.password=

Save the file (Ctrl+S).


----------------------------------------------------------------
STEP 9 — RUN THE PROJECT
----------------------------------------------------------------

In the VS Code terminal, run:
   ./mvnw clean spring-boot:run

The first time will take 2-3 minutes to download dependencies.
Wait until you see:

   Started LabmsApplication in x.xxx seconds

Then open your browser and go to:
   http://localhost:8080

You should see the login page. If you do — setup is complete!


----------------------------------------------------------------
STEP 10 — START WORKING ON YOUR MODULE
----------------------------------------------------------------

Your module folder is inside:
   src/main/java/com/companya/labms/YOUR_MODULE_NAME/

Each team must build:
   1. Entity.java         — your database table (extends BaseEntity)
   2. Repository.java     — talks to the database
   3. Service.java        — your business logic
   4. Controller.java     — your REST API endpoints
   5. A frontend HTML page in src/main/resources/static/

After making changes, always commit to YOUR branch:
   git add .
   git commit -m "describe what you did"
   git push origin feature/YOUR_MODULE_NAME

Do NOT push to main. The team lead (Himasha) will merge.


----------------------------------------------------------------
IMPORTANT RULES
----------------------------------------------------------------

  * Always pull latest changes before starting work:
      git pull origin feature/YOUR_MODULE_NAME

  * Never commit directly to main(again very important)!!!

  * Your JWT token for testing protected endpoints:
      After login, copy the token from sessionStorage
      Add it to requests as:
      Header: Authorization: Bearer YOUR_TOKEN_HERE

  * If the project won't start, check:
      1. Is MySQL running? (search "Services" in Windows,
         find MySQL80, make sure it says Running)
      2. Is the database created? (Step 4)
      3. Is your password correct in application.properties?

  * If you get JAVA_HOME error, run this in PowerShell
    (as Administrator) and restart VS Code:
      [System.Environment]::SetEnvironmentVariable("JAVA_HOME","C:\Program Files\Eclipse Adoptium\jdk-17.0.14.7-hotspot","Machine")


----------------------------------------------------------------
CONTACT Me if you got any issues
----------------------------------------------------------------

If you're stuck, message me.
Do NOT guess and break things on main branch.

GitHub Repo:
https://github.com/himashafdo/CS2833-Laboratory-Management-System.git

================================================================
