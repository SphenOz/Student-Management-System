package JDBC;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/student_db";
        String user = "root";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            System.out.println("Connected to MySQL.");

            // Create and use the database
            stmt.execute("CREATE DATABASE IF NOT EXISTS student_db");
            stmt.execute("USE student_db");

            // Run create_schema.sql
            String createSQL = Files.readString(Paths.get("create_schema.sql"));
            for (String sql : createSQL.split(";")) {
                if (!sql.strip().isEmpty()) {
                    stmt.execute(sql);
                }
            }
            System.out.println("Schema created.");

            // Run initialize_data.sql
            String initSQL = Files.readString(Paths.get("initialize_data.sql"));
            for (String sql : initSQL.split(";")) {
                if (!sql.strip().isEmpty()) {
                    stmt.execute(sql);
                }
            }
            System.out.println("Data initialized.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}