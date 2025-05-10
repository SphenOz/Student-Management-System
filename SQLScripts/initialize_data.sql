-- Students
-- (student_id, first_name, last_name, email, date_of_birth, major)
INSERT INTO Students VALUES
(1, 'Peter', 'Houston', 'peter.houston@sjsu.edu', '2002-04-10', 'Computer Science'),
(2, 'Tara', 'Robinson', 'tara.robinson@sjsu.edu', '2001-08-22', 'Economics'),
(3, 'Yvette', 'Ross', 'yvette.ross@sjsu.edu', '2003-01-05', 'Computer Science'),
(4, 'Cliff', 'Parker', 'cliff.parker@sjsu.edu', '2002-11-23', 'Statistics'),
(5, 'Carla', 'George', 'carla.george@sjsu.edu', '2000-12-02', 'Math'),
(6, 'Leanne', 'Parsons', 'leanne.parsons@sjsu.edu', '2001-03-15', 'Computer Engineering'),
(7, 'Jenna', 'Cohen', 'jenna.cohen@sjsu.edu', '2002-07-07', 'Aerospace Engineering'),
(8, 'Maggie', 'Meyers', 'maggie.meyers@sjsu.edu', '2003-05-30', 'Communications'),
(9, 'Curt', 'Fowler', 'curt.fowler@sjsu.edu', '2001-06-10', 'Political Science'),
(10, 'Meredith', 'Walls', 'meredith.walls@sjsu.edu', '2002-01-12', 'Biology'),
(11, 'Jessie', 'Hammond', 'jessie.hammond@sjsu.edu', '2000-09-09', 'Biomedical Engineering'),
(12, 'Colleen', 'Juarez', 'colleen.juarez@sjsu.edu', '2001-10-03', 'Business'),
(13, 'Jerome', 'Wallace', 'jerome.wallace@sjsu.edu', '2003-02-27', 'Accounting'),
(14, 'Jeff', 'Chan', 'jeff.chan@sjsu.edu', '2002-04-18', 'Management Information Systems'),
(15, 'Cecilia', 'Dougherty', 'cecilia.dougherty@sjsu.edu', '2000-08-29', 'Physics');

-- Professors
-- (professor_id, first_name, last_name, email)
INSERT INTO Professors (professor_id, first_name, last_name, email) VALUES
(101, 'William', 'Smith', 'william.smith@sjsu.edu'),
(102, 'Tammy', 'Chan', 'tammy.chan@sjsu.edu'),
(103, 'Andy', 'Meyers', 'andy.meyers@sjsu.edu'),
(104, 'Tahereh', 'Arabghalizi', 'tahereh.arabghalizi@sjsu.edu'),
(105, 'Scott', 'Walker', 'scott.walker@sjsu.edu'),
(106, 'Ryan', 'Nguyen', 'ryan.nguyen@sjsu.edu'),
(107, 'George', 'Hill', 'george.hill@sjsu.edu'),
(108, 'Nick', 'Miller', 'nick.miller@sjsu.edu'),
(109, 'Chris', 'Webber', 'chris.webber@sjsu.edu'),
(110, 'Clay', 'Matthews', 'clay.matthews@sjsu.edu'),
(111, 'Tiffany', 'Tran', 'tiffany.tran@sjsu.edu'),
(112, 'Karen', 'Harvey', 'karen.harvey@sjsu.edu'),
(113, 'John', 'Johnson', 'john.johnson@sjsu.edu'),
(114, 'Chris', 'Brown', 'chris.brown@sjsu.edu'),
(115, 'Tony', 'Parker', 'tony.parker@sjsu.edu');

-- Courses
-- (course_id, course_name, course_code, professor_id, credits)
INSERT INTO Courses VALUES
(201, 'Aerodynamics I', 'AE 160', 101, 3),
(202, 'Calculus I', 'MATH 30', 103, 3),
(203, 'Calculus II', 'MATH 31', 107, 4),
(204, 'Introduction to Database Management Systems', 'CS 157A', 104, 3),
(205, 'Public Speaking', 'COMM 20', 110, 3),
(206, 'Neuroscience', 'BIOL 129', 108, 3),
(207, 'Digital Design I', 'CMPE 124', 105, 3),
(208, 'Sales Management', 'BUS2 135', 112, 3),
(209, 'Public Finance', 'ECON 132', 115, 3),
(210, 'Financial Accounting', 'BUS1 20', 106, 3),
(211, 'Medical Engineering Design Methods', 'BME 135', 109, 3),
(212, 'Law and Society', 'POLS 120', 102, 3),
(213, 'Lasers', 'PHYS 168', 111, 3),
(214, 'Health Economics', 'ECON 170', 113, 3),
(215, 'Biochemistry', 'CHEM130A', 114, 4);

-- Enrollments
-- (enrollment_id, student_id, course_id, semester, year)
INSERT INTO Enrollments VALUES
(301, 1, 204, 'Spring', 2025),
(302, 2, 214, 'Spring', 2024),
(303, 3, 203, 'Fall', 2025),
(304, 4, 203, 'Spring', 2025),
(305, 5, 202, 'Fall', 2023),
(306, 6, 207, 'Fall', 2022),
(307, 7, 201, 'Summer', 2022),
(308, 8, 205, 'Fall', 2021),
(309, 9, 212, 'Spring', 2024),
(310, 10, 206, 'Summer', 2024),
(311, 11, 211, 'Summer', 2023),
(312, 12, 208, 'Spring', 2023),
(313, 13, 210, 'Fall', 2024),
(314, 14, 208, 'Spring', 2025),
(315, 15, 213, 'Fall', 2021);

-- Grades
-- (grade_id, enrollment_id, grade)
INSERT INTO Grades VALUES
(401, 301, 'A'),
(402, 302, 'F'),
(403, 303, 'C'),
(404, 304, 'D'),
(405, 305, 'A'),
(406, 306, 'A'),
(407, 307, 'B'),
(408, 308, 'C'),
(409, 309, 'B'),
(410, 310, 'B'),
(411, 311, 'A'),
(412, 312, 'C'),
(413, 313, 'F'),
(414, 314, 'A'),
(415, 315, 'B');