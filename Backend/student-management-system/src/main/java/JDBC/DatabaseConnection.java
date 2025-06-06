package JDBC;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.srm.student_management_system.controller.Course;
import com.srm.student_management_system.controller.EnrollmentInfo;
import com.srm.student_management_system.controller.Professor;
import com.srm.student_management_system.controller.Student;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/student_db";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        initializeDatabase();  // Reset DB with latest schema/data

        // 1. Create a new student
        createStudent("Test", "Student", "test.student@sjsu.edu", "2001-01-01", "Test Major");

        // 2. Read all students
        System.out.println("\n--- All Students ---");
        readStudents();

        // 3. Update student email (student_id 1)
        updateStudentEmail(1, "peter.houston.updated@sjsu.edu");

        // 4. Enroll student_id 2 into course_id 201 (assumes course 201 exists)
        enrollInCourse(2, 201);

        // 5. Drop student_id 2 from course_id 201
        dropFromCourse(2, 201);  // Should succeed

        // 6. Professor 101 drops student_id 3 from course 201 (only if enrolled and prof teaches it)
        dropStudentFromCourse(101, 3, 201);

        // 7. Try dropping student not enrolled
        dropFromCourse(2, 999);  // Should say not enrolled

        // 8. Professor 102 (wrong prof) tries to drop student 2 from course 201
        dropStudentFromCourse(102, 2, 201);  // Should say permission denied

        // 9. Delete student 4 (ensure cascading delete of enrollments/grades)
        //deleteStudent(4);

        // 10. Final state
        System.out.println("\n--- Final Students ---");
        readStudents();
        // View all grades for student with id 2
        viewGrades(2);

        //View classes taught by professor with id 102
        viewClassesByProf(102);

        //View classes enrolled in for student with id 2
        viewEnrolled(2);

        findStudent(2);

        findProf(102);

        showCourses();
        updateGrade(101, 2, 201, "A");
        updateGrade(102, 2, 201, "B");

    }

    // Initialize the database and create tables
    // and insert initial data
    public static void initializeDatabase() {
        String basePath = "SQLScripts/";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            System.out.println("Connected to MySQL.");
            stmt.execute("CREATE DATABASE IF NOT EXISTS student_db");
            stmt.execute("USE student_db");

            String createSQL = Files.readString(Paths.get(basePath + "create_schema.sql"));
            for (String sql : createSQL.split(";")) {
                if (!sql.strip().isEmpty()) stmt.execute(sql);
            }

            String initSQL = Files.readString(Paths.get(basePath + "initialize_data.sql"));
            for (String sql : initSQL.split(";")) {
                if (!sql.strip().isEmpty()) stmt.execute(sql);
            }

            System.out.println("Database initialized.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    // Create a new student
    // This method uses a helper method to get the next student ID
    public static void createStudent(String firstName, String lastName, String email, String dob, String major) {
        String sql = "INSERT INTO Students (student_id, first_name, last_name, email, date_of_birth, major) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, getNextStudentId(conn));
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, email);
            pstmt.setDate(5, Date.valueOf(dob));  // Format: "YYYY-MM-DD"
            pstmt.setString(6, major);
            pstmt.executeUpdate();
            System.out.println("Student created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Find a student by ID
    public static Student findStudent(int id){
        Student s = new Student();
        String sql = "SELECT * FROM Students WHERE student_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery(); 
            while (rs.next()) {
                s.setId(rs.getInt("student_id"));
                s.setFirstName(rs.getString("first_name")); 
                s.setLastName(rs.getString("last_name"));
                s.setEmail(rs.getString("email")); 
                s.setDate(rs.getDate("date_of_birth")); 
                s.setMajor(rs.getString("major"));
                //System.out.println("return student: " + s.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }

    // Get the next student ID
    // This method is used in createStudent to ensure unique IDs
    private static int getNextStudentId(Connection conn) throws SQLException {
        String sql = "SELECT MAX(student_id) FROM Students";
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        }
        return 1;
    }
    private static int getNextCourseID(Connection conn) throws SQLException {
        String sql = "SELECT MAX(course_id) FROM Courses";
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        }
        return 1;
    }

    // Read all students and return as a list
    public static List<Student> readStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Students";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student();
                    s.setId(rs.getInt("student_id"));
                    s.setFirstName(rs.getString("first_name")); 
                    s.setLastName(rs.getString("last_name"));
                    s.setEmail(rs.getString("email")); 
                    s.setDate(rs.getDate("date_of_birth")); 
                    s.setMajor(rs.getString("major"));
                students.add(s);
                System.out.printf("ID: %d, Name: %s %s, Email: %s, DOB: %s, Major: %s%n",
                    rs.getInt("student_id"), rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("email"), rs.getDate("date_of_birth"), rs.getString("major"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    // Update student email by ID
    public static void updateStudentEmail(int id, String newEmail) {
        String sql = "UPDATE Students SET email = ? WHERE student_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEmail);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Student email updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Update grade for a student in a course
    public static void updateGrade(int professorId, int studentId, int courseId, String newGrade) {
        String verifyProfessorSql = "SELECT COUNT(*) FROM Courses WHERE course_id = ? AND professor_id = ?";
        String verifyEnrollmentSql = "SELECT enrollment_id FROM Enrollments WHERE student_id = ? AND course_id = ?";
        String updateGradeSql = "UPDATE Grades SET grade = ? WHERE enrollment_id = ?";
    
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
    
            // Step 1: Check if the professor teaches the course
            try (PreparedStatement ps1 = conn.prepareStatement(verifyProfessorSql)) {
                ps1.setInt(1, courseId);
                ps1.setInt(2, professorId);
                ResultSet rs1 = ps1.executeQuery();
                if (rs1.next() && rs1.getInt(1) == 0) {
                    System.out.println("Permission denied: Professor does not teach this course.");
                    return;
                }
            }
    
            // Step 2: Check if student is enrolled and get the enrollment_id
            int enrollmentId = -1;
            try (PreparedStatement ps2 = conn.prepareStatement(verifyEnrollmentSql)) {
                ps2.setInt(1, studentId);
                ps2.setInt(2, courseId);
                ResultSet rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    enrollmentId = rs2.getInt("enrollment_id");
                } else {
                    System.out.println("Student is not enrolled in this course.");
                    return;
                }
            }
    
            // Step 3: Update grade
            try (PreparedStatement ps3 = conn.prepareStatement(updateGradeSql)) {
                ps3.setString(1, newGrade);
                ps3.setInt(2, enrollmentId);
                int rowsAffected = ps3.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Grade updated successfully.");
                } else {
                    System.out.println("Grade update failed (no record found).");
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Delete a student and all dependent records (enrollments and grades)
    public static void deleteStudent(int id) {
        String deleteGrades = "DELETE FROM Grades WHERE enrollment_id IN (SELECT enrollment_id FROM Enrollments WHERE student_id = ?)";
        String deleteEnrollments = "DELETE FROM Enrollments WHERE student_id = ?";
        String deleteStudent = "DELETE FROM Students WHERE student_id = ?";
    
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false); // Start transaction
    
            try (PreparedStatement ps1 = conn.prepareStatement(deleteGrades);
                 PreparedStatement ps2 = conn.prepareStatement(deleteEnrollments);
                 PreparedStatement ps3 = conn.prepareStatement(deleteStudent)) {
    
                ps1.setInt(1, id);
                ps1.executeUpdate();
    
                ps2.setInt(1, id);
                ps2.executeUpdate();
    
                ps3.setInt(1, id);
                ps3.executeUpdate();
    
                conn.commit();
                System.out.println("Student and all dependent records deleted.");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Show all courses and return as a list
    public static List<Course> showCourses(){
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Courses";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Course c = new Course();
                    c.setCourseID(rs.getInt("course_id"));
                    c.setCourseName(rs.getString("course_name")); 
                    c.setCourseCode(rs.getString("course_code"));
                    c.setProfessorID(rs.getInt("professor_id")); 
                    c.setCredits(rs.getInt("credits")); 
                courses.add(c);
                //System.out.printf("ID: %d, Name: %s, Code: %s, ProfID: %d, Credits: %d%n",
                //    rs.getInt("course_id"), rs.getString("course_name"), rs.getString("course_code"),
                //    rs.getInt("professor_id"), rs.getInt("credits"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    // Find a course by ID and return as a Course object
    public static Course findCourse(int id){
        Course c = new Course();
        String sql = "SELECT * FROM Courses WHERE course_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery(); 
            while (rs.next()) {
                c.setCourseID(rs.getInt("course_id"));
                c.setCourseName(rs.getString("course_name")); 
                c.setCourseCode(rs.getString("course_code"));
                c.setProfessorID(rs.getInt("professor_id")); 
                c.setCredits(rs.getInt("credits")); 
                //System.out.println("return course: " + c.getCourseID());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }
    
    // Create a new course
    // This method uses a helper method to get the next course ID
    public static void createCourse(String name, String code, int professorID, int credits) {
        String sql = "INSERT INTO Courses (course_id, course_name, course_code, professor_id, credits) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, getNextCourseID(conn));
            pstmt.setString(2, name);
            pstmt.setString(3, code);
            pstmt.setInt(4, professorID);
            pstmt.setInt(5, credits);
            pstmt.executeUpdate();
            System.out.println("Course created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Enroll a student in a course
    // This method uses a helper method to get the next enrollment ID
    public static void enrollInCourse(int studentId, int courseId) {
        String enrollmentSQL = "INSERT INTO Enrollments (enrollment_id, student_id, course_id, semester, year) VALUES (?, ?, ?, ?, ?)";
        String gradeSQL = "INSERT INTO Grades (grade_id, enrollment_id, grade) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);  // Start transaction

            int enrollmentId = getNextEnrollmentId(conn, "Enrollments", "enrollment_id");
            int gradeId = getNextEnrollmentId(conn, "Grades", "grade_id");  // Reuse same ID generator logic

            try (PreparedStatement pstmtEnroll = conn.prepareStatement(enrollmentSQL);
                PreparedStatement pstmtGrade = conn.prepareStatement(gradeSQL)) {

                // Insert enrollment
                pstmtEnroll.setInt(1, enrollmentId);
                pstmtEnroll.setInt(2, studentId);
                pstmtEnroll.setInt(3, courseId);
                pstmtEnroll.setString(4, "Fall");
                pstmtEnroll.setInt(5, 2025);
                pstmtEnroll.executeUpdate();

                // Insert default grade
                pstmtGrade.setInt(1, gradeId);
                pstmtGrade.setInt(2, enrollmentId);
                pstmtGrade.setString(3, "I");
                pstmtGrade.executeUpdate();

                conn.commit();
                System.out.println("Student enrolled in course and grade initialized to 'I'.");
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get the next enrollment ID
    private static int getNextEnrollmentId(Connection conn, String table, String column) throws SQLException {
        String sql = "SELECT MAX(" + column + ") FROM " + table;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1) + 1;
        }
        return 1;
    }

    // Drop a student from a course
    // This method checks if the student is enrolled before dropping
    public static void dropFromCourse(int studentId, int courseId) {
        String checkEnrollment = "SELECT COUNT(*) FROM Enrollments WHERE student_id = ? AND course_id = ?";
        String deleteGrades = "DELETE FROM Grades WHERE enrollment_id IN (SELECT enrollment_id FROM Enrollments WHERE student_id = ? AND course_id = ?)";
        String deleteEnrollment = "DELETE FROM Enrollments WHERE student_id = ? AND course_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Check if the student is enrolled
            try (PreparedStatement checkStmt = conn.prepareStatement(checkEnrollment)) {
                checkStmt.setInt(1, studentId);
                checkStmt.setInt(2, courseId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("Student is not enrolled in the specified course.");
                    return;
                }
            }
    
            conn.setAutoCommit(false);
    
            try (PreparedStatement ps1 = conn.prepareStatement(deleteGrades);
                 PreparedStatement ps2 = conn.prepareStatement(deleteEnrollment)) {
    
                ps1.setInt(1, studentId);
                ps1.setInt(2, courseId);
                ps1.executeUpdate();
    
                ps2.setInt(1, studentId);
                ps2.setInt(2, courseId);
                ps2.executeUpdate();
    
                conn.commit();
                System.out.println("Student dropped from course.");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Drop a student from a course by a professor
    // This method checks if the professor teaches the course before dropping
    public static void dropStudentFromCourse(int professorId, int studentId, int courseId) {
        String verifySql = "SELECT COUNT(*) FROM Courses WHERE course_id = ? AND professor_id = ?";
        String deleteGrades = "DELETE FROM Grades WHERE enrollment_id IN (SELECT enrollment_id FROM Enrollments WHERE student_id = ? AND course_id = ?)";
        String deleteEnrollment = "DELETE FROM Enrollments WHERE student_id = ? AND course_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Verifies that the professor teaches the course
            try (PreparedStatement checkStmt = conn.prepareStatement(verifySql)) {
                checkStmt.setInt(1, courseId);
                checkStmt.setInt(2, professorId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("Permission denied: Professor does not teach course.");
                    return;
                }
            }

            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(deleteGrades);
                PreparedStatement ps2 = conn.prepareStatement(deleteEnrollment)) {
                    
                    ps1.setInt(1, studentId);
                    ps1.setInt(2, courseId);
                    ps1.executeUpdate();

                    ps2.setInt(1, studentId);
                    ps2.setInt(2, courseId);
                    ps2.executeUpdate();

                    conn.commit();
                    System.out.println("Student dropped from course by Professor.");
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    
    // View all grades for a student
    public static void viewGrades(int id){
        String sql = "SELECT course_name, grade " + 
                        "FROM Students S " + 
                        "JOIN Enrollments e ON S.student_id = e.student_id " + 
                        "JOIN Courses c ON e.course_id = c.course_id " + 
                        "JOIN Grades g ON e.enrollment_id = g.enrollment_id " + 
                        "WHERE S.student_id = ?;";
        
        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                
                boolean found = false;
                while(rs.next()){
                    String courseName = rs.getString("course_name");
                    String grade = rs.getString("grade");

                    System.out.println("Grades for " + id + ": " + courseName + " " + grade);
                    found = true;
                }

                if(!found){
                    System.out.println("No grades for Student " + id);
                }

            } catch(SQLException e){
                e.printStackTrace();
            }
    }

    // View all classes taught by a professor and return as a list of Course objects
    public static List<Course> viewClassesByProf(int id){
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Courses WHERE professor_id = ?";
        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)){

                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                
                while(rs.next()){
                    Course c = new Course();
                    c.setCourseID(rs.getInt("course_id"));
                    c.setCourseName(rs.getString("course_name"));
                    c.setCourseCode(rs.getString("course_code"));
                    c.setProfessorID(rs.getInt("professor_id"));
                    c.setCredits(rs.getInt("credits"));
                    courses.add(c);

                    System.out.println("Courses taught by Professor " + c.getProfessorID() + ": " + c.getCourseID() + " " + c.getCourseName()
                        + " " + c.getCourseCode() + " " + c.getCredits());
                }

            } catch(SQLException e){
                e.printStackTrace();
            }
            return courses;
    }

    // Find a professor by ID and return as a Professor object
    public static Professor findProf(int id){
        Professor p = new Professor();
        String sql = "SELECT * FROM Professors WHERE professor_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery(); 
            while (rs.next()) {
                p.setId(rs.getInt("professor_id"));
                p.setFirstName(rs.getString("first_name")); 
                p.setLastName(rs.getString("last_name"));
                p.setEmail(rs.getString("email")); 
                System.out.println("return prof: " + p.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

    // View all classes enrolled in by a student and return as a list of EnrollmentInfo objects
    public static List<EnrollmentInfo> viewEnrolled(int id){
        List<EnrollmentInfo> EI = new ArrayList<>();
        String sql = "SELECT s.student_id, c.course_id, e.enrollment_id, c.course_name, c.course_code, c.credits, e.semester, e.year, p.last_name " + 
                        "FROM Students s " + 
                        "JOIN Enrollments e ON s.student_id = e.student_id " + 
                        "JOIN Courses c ON e.course_id = c.course_id " + 
                        "JOIN Professors p ON c.professor_id = p.professor_id " + 
                        "WHERE s.student_id = ?;";
        try(Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)){
            
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                            
                boolean found = false;
                while(rs.next()){
                    EnrollmentInfo e = new EnrollmentInfo();
                    e.setStudentId(rs.getInt("student_id"));
                    e.setCourseId(rs.getInt("course_id"));
                    e.setEnrollmentId(rs.getInt("enrollment_id"));
                    e.setCourseName(rs.getString("course_name"));
                    e.setCourseCode(rs.getString("course_code"));
                    e.setCourseCredits(rs.getInt("credits"));
                    e.setSemester(rs.getString("semester"));
                    e.setYear(rs.getInt("year"));
                    e.setProfLastName(rs.getString("last_name"));
                    EI.add(e);
                    
                    //System.out.println("Classes for " + id + ": " + e.getStudentId() + " " + e.getCourseId() + " " + e.getEnrollmentId() + 
                    //    " " + e.getCourseName() + " " + e.getCourseCode() + " " + e.getCourseCredits() + " " + e.getSemester() + " " + e.getYear() + " " + e.getProfLastName());
                    found = true;
                }
            
                if(!found){
                    System.out.println("No grades for Student " + id);
                }
            
            } catch(SQLException e){
                e.printStackTrace();
            }
            //System.out.println("Classes for " + id + ": " + EI.size() + " classes found.");
        return EI;
                
    }
     
    // View all students enrolled in a course and return as a list of Student objects
    public static List<Student> viewStudentsByCourse(int courseId) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.student_id, s.first_name, s.last_name, s.email, s.date_of_birth, s.major " +
                    "FROM Students s " +
                    "JOIN Enrollments e ON s.student_id = e.student_id " +
                    "WHERE e.course_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getInt("student_id"));
                s.setFirstName(rs.getString("first_name"));
                s.setLastName(rs.getString("last_name"));
                s.setEmail(rs.getString("email"));
                s.setDate(rs.getDate("date_of_birth"));
                s.setMajor(rs.getString("major"));
                students.add(s);

                System.out.printf("Student ID: %d, Name: %s %s, Email: %s, DOB: %s, Major: %s%n",
                    s.getId(), s.getFirstName(), s.getLastName(), s.getEmail(), s.getDate(), s.getMajor());
            }

            if (students.isEmpty()) {
                System.out.println("No students enrolled in course ID: " + courseId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    // Get the grade for a specific enrollment ID
    public static String getGrade(int enrollmentId) {
        String sql = "SELECT grade FROM Grades WHERE enrollment_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, enrollmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("grade");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    

}
