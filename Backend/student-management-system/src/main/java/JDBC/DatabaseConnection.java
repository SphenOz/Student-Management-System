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
        initializeDatabase();
        createStudent("Alice", "Johnson", "alice.johnson@sjsu.edu", "2002-05-06", "Computer Science");
        readStudents();
        updateStudentEmail(1, "alice.new@sjsu.edu");
        deleteStudent(1);
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
}