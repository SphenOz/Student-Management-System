-- Drop tables if they exist
DROP TABLE IF EXISTS Grades;
DROP TABLE IF EXISTS Enrollments;
DROP TABLE IF EXISTS Courses;
DROP TABLE IF EXISTS Professors;
DROP TABLE IF EXISTS Students;

-- Students Table
CREATE TABLE Students (
    student_id INT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    major VARCHAR(100) NOT NULL
);

-- Professors Table
CREATE TABLE Professors (
    professor_id INT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- Courses Table
CREATE TABLE Courses (
    course_id INT PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    course_code VARCHAR(10) NOT NULL UNIQUE,
    professor_id INT NOT NULL,
    credits INT NOT NULL CHECK (credits > 0),
    FOREIGN KEY (professor_id) REFERENCES Professors(professor_id)
);

-- Enrollments Table
CREATE TABLE Enrollments (
    enrollment_id INT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    semester VARCHAR(10) NOT NULL,
    year INT NOT NULL CHECK (year >= 2000),
    FOREIGN KEY (student_id) REFERENCES Students(student_id),
    FOREIGN KEY (course_id) REFERENCES Courses(course_id)
);

-- Grades Table
CREATE TABLE Grades (
    grade_id INT PRIMARY KEY,
    enrollment_id INT NOT NULL,
    grade VARCHAR(2) NOT NULL CHECK (grade IN ('A', 'B', 'C', 'D', 'F', 'I')),
    FOREIGN KEY (enrollment_id) REFERENCES Enrollments(enrollment_id)
);