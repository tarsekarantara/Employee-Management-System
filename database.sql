CREATE DATABASE IF NOT EXISTS employee_management_db;
USE employee_management_db;

CREATE TABLE IF NOT EXISTS employees (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    department VARCHAR(100) NOT NULL,
    job_role VARCHAR(100) NOT NULL,
    salary DECIMAL(12,2) NOT NULL DEFAULT 0
);

INSERT IGNORE INTO employees (id, name, email, department, job_role, salary) VALUES
(1, 'Rahul Sharma', 'rahul@example.com', 'IT', 'Java Developer', 35000.00),
(2, 'Priya Patil', 'priya@example.com', 'HR', 'HR Executive', 30000.00);
