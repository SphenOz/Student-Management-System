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
(1001, 1, 104, 'Spring', 2025),
(1002, 2, 114, 'Spring', 2025),
(1003, 3, 103, 'Fall', 2025),
(1004, 4, 103, 'Spring', 2025),
(1005, 5, 102, 'Fall', 2025),
(1006, 6, 107, 'Fall', 2025),
(1007, 7, 101, 'Summer', 2025),
(1008, 8, 105, 'Fall', 2025),
(1009, 9, 112, 'Spring', 2025),
(1010, 10, 106, 'Summer', 2025),
(1011, 11, 111, 'Summer', 2025),
(1012, 12, 108, 'Spring', 2025),
(1013, 13, 110, 'Fall', 2025),
(1014, 14, 108, 'Spring', 2025),
(1015, 15, 113, 'Fall', 2025);

-- Grades
-- (grade_id, enrollment_id, grade)
(501, 1001, 'A'),
(502, 1002, 'F'),
(503, 1003, 'C'),
(504, 1004, 'D'),
(505, 1005, 'A'),
(506, 1006, 'A'),
(507, 1007, 'B'),
(508, 1008, 'C'),
(509, 1009, 'B'),
(510, 1010, 'B'),
(511, 1011, 'A'),
(512, 1012, 'C'),
(513, 1013, 'F'),
(514, 1014, 'A'),
(515, 1015, 'B');