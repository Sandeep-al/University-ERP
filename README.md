# 🎓 University ERP System

A robust desktop-based Enterprise Resource Planning (ERP) application designed to manage the core academic processes of a university. The user interface is built using **Java Swing**, and it uses **MySQL** as the database backend. 

Prioritizing security and data integrity, this project implements a **Dual-Database Strategy** and utilizes **jBCrypt** for password hashing.

## 🚀 Key Features

* **Role-Based Access Control (RBAC):** Separate, secure dashboards for Admins, Professors, and Students.
* **Dual-Database Architecture:** Login credentials and university operational data are stored in separate databases to maximize security.
* **Maintenance Mode:** Admins can toggle the system into maintenance mode with a single click, preventing other users from performing write operations while the system is undergoing updates.
* **Flexible Grading System:** Professors can assign marks (0-100) and letter grades independently using an SQL "Upsert" strategy.

## 🛠️ Tech Stack

* **Language:** Java (JDK 17+)
* **Frontend UI:** Java Swing (JFrame, JPanel)
* **Database:** MySQL (JDBC for connectivity)
* **Security:** jBCrypt (Password Hashing)

## 🗄️ Database Schema

This project utilizes two separate databases. The complete setup can be achieved using the SQL scripts provided in the `edu/univ/erp/schema/` folder.

### 1. Authentication Database (`authenticationdb`)
This database exclusively handles user identities and secure login authentication.
* **`users_auth` Table:**
    * `user_id` (INT, Primary Key)
    * `username` (VARCHAR)
    * `password_hash` (VARCHAR) - Hashed using jBCrypt
    * `role` (VARCHAR) - Defines the access level (admin, student, professor)
    * `status` (VARCHAR)
    * `last_login` (DATETIME)

### 2. University Database (`universitydb`)
This database stores the operational data required for the ERP's daily functions.
* **`UNI_USERS` Table:** Profile data for students and professors. Columns: `id` (PK), `username`, `password`, `role` (ENUM).
* **`COURSES` Table:** Course catalog. Columns: `id` (PK), `code`, `name`, `credits`, `professor_id` (Foreign Key).
* **`REGISTRATIONS` Table:** Links students to their enrolled courses. Columns: `id` (PK), `student_id` (FK), `course_id` (FK).
* **`GRADES` Table:** Academic performance data. Columns: `id` (PK), `registration_id` (FK), `marks`, `letter_grade`.

## ⚙️ How to Setup & Run (Local Installation)

Follow these simple steps to run the project on your local machine:

### Step 1: Database Setup
1. Create two new databases in your MySQL server: `authenticationdb` and `universitydb`.
2. Navigate to the `edu/univ/erp/schema/` folder in your project directory.
3. Import and run the provided SQL scripts in your MySQL instance to automatically generate all required tables and foreign key relationships.

### Step 2: Seed Admin User (Initial Login)
To access the system for the first time, a default Admin account must be generated:
1. Open the path `edu/univ/erp/util/` in your IDE.
2. Run the `SeedAuthData.java` file.
3. This will create a default admin in the database with the following credentials:
   * **Username:** `admin`
   * **Password:** `adminpass`

### Step 3: Start the Application
1. Once the database and auth setup is complete, run the `Main.java` file located in the root directory.
2. When the login window launches, enter the admin credentials provided above to access the dashboard!

## 👨‍💻 Developers
* **Sandeep Kumar** (Roll No: 2024504)
* **Prem Gupta** (Roll No: 2024435)
