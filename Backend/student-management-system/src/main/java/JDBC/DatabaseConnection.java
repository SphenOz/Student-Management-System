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
        enrollInCourse(2, 201, "Fall", 2025);

        // 5. Drop student_id 2 from course_id 201
        dropFromCourse(2, 201);  // Should succeed

        // 6. Professor 101 drops student_id 3 from course 201 (only if enrolled and prof teaches it)
        dropStudentFromCourse(101, 3, 201);

        // 7. Try dropping student not enrolled
        dropFromCourse(2, 999);  // Should say not enrolled

        // 8. Professor 102 (wrong prof) tries to drop student 2 from course 201
        dropStudentFromCourse(102, 2, 201);  // Should say permission denied

        // 9. Delete student 4 (ensure cascading delete of enrollments/grades)
        deleteStudent(4);

        // 10. Final state
        System.out.println("\n--- Final Students ---");
        readStudents();
    }

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

    public static void readStudents() {
        String sql = "SELECT * FROM Students";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s %s, Email: %s, DOB: %s, Major: %s%n",
                    rs.getInt("student_id"), rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("email"), rs.getDate("date_of_birth"), rs.getString("major"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    public static void createCourse(int courseId, String name, String code, String instructor, int credits) {
        String sql = "INSERT INTO Courses (course_id, course_name, course_code, instructor, credits) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);
            pstmt.setString(2, name);
            pstmt.setString(3, code);
            pstmt.setString(4, instructor);
            pstmt.setInt(5, credits);
            pstmt.executeUpdate();
            System.out.println("Course created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void enrollInCourse(int studentId, int courseId, String semester, int year) {
        String sql = "INSERT INTO Enrollments (enrollment_id, student_id, course_id, semester, year) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int nextId = getNextEnrollmentId(conn, "Enrollments", "enrollment_id");
            pstmt.setInt(1, nextId);
            pstmt.setInt(2, studentId);
            pstmt.setInt(3, courseId);
            pstmt.setString(4, semester);
            pstmt.setInt(5, year);
            pstmt.executeUpdate();
            System.out.println("Student enrolled in course.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getNextEnrollmentId(Connection conn, String table, String column) throws SQLException {
        String sql = "SELECT MAX(" + column + ") FROM " + table;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1) + 1;
        }
        return 1;
    }

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
}