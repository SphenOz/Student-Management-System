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

-- Courses
-- (course_id, course_name, course_code, instructor, credits)
INSERT INTO Courses VALUES
(101, 'Aerodynamics I', 'AE 160', 'Smith', 3),
(102, 'Calculus I', 'MATH 30', 'Chan', 3),
(103, 'Calculus II', 'MATH 31', 'Meyers', 4),
(104, 'Introduction to Database Management Systems', 'CS 157A', 'Arabghalizi', 3),
(105, 'Public Speaking', 'COMM 20', 'Walker', 3),
(106, 'Neuroscience', 'BIOL 129', 'Nguyen', 3),
(107, 'Digital Design I', 'CMPE 124', 'Hill', 3),
(108, 'Sales Management', 'BUS2 135', 'Miller', 3),
(109, 'Public Finance', 'ECON 132', 'Webber', 3),
(110, 'Financial Accounting', 'BUS1 20', 'Matthews', 3),
(111, 'Medical Engineering Design Methods', 'BME 135', 'Tran', 3),
(112, 'Law and Society', 'POLS 120', 'Harvey', 3),
(113, 'Lasers', 'PHYS 168', 'Johnson', 3),
(114, 'Health Economics', 'ECON 170', 'Brown', 3),
(115, 'Biochemistry', 'CHEM130A', 'Parker', 4);

-- Enrollments
-- (enrollment_id, student_id, course_id, semester, year)
INSERT INTO Enrollments VALUES
(201, 1, 104, 'Spring', 2025),
(202, 2, 114, 'Spring', 2024),
(203, 3, 103, 'Fall', 2025),
(204, 4, 103, 'Spring', 2025),
(205, 5, 102, 'Fall', 2023),
(206, 6, 107, 'Fall', 2022),
(207, 7, 101, 'Summer', 2022),
(208, 8, 105, 'Fall', 2021),
(209, 9, 112, 'Spring', 2024),
(210, 10, 106, 'Summer', 2024),
(211, 11, 111, 'Summer', 2023),
(212, 12, 108, 'Spring', 2023),
(213, 13, 110, 'Fall', 2024),
(214, 14, 108, 'Spring', 2025),
(215, 15, 113, 'Fall', 2021);

-- Grades
-- (grade_id, enrollment_id, grade)
INSERT INTO Grades VALUES
(301, 201, 'A'),
(302, 202, 'F'),
(303, 203, 'C'),
(304, 204, 'D'),
(305, 205, 'A'),
(306, 206, 'A'),
(307, 207, 'B'),
(308, 208, 'C'),
(309, 209, 'B'),
(310, 210, 'B'),
(311, 211, 'A'),
(312, 212, 'C'),
(313, 213, 'F'),
(314, 214, 'A'),
(315, 215, 'B');