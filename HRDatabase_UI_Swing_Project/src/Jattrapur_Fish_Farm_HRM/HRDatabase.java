package Jattrapur_Fish_Farm_HRM;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HRDatabase {

    // NOTE: Relative DB paths can accidentally create multiple DB files depending on run directory.
    // Printing the absolute path helps you verify you're connecting to the DB you expect.
    private static final String DB_FILE = "hrApplicant.db";
    private static final String URL = "jdbc:sqlite:" + new File(DB_FILE).getAbsolutePath();

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }
    // Create table
    public void createTable() {
        // Updated to include the 'user' table
        String sqlApplicants = """
                CREATE TABLE IF NOT EXISTS applicants (
                    id INTEGER PRIMARY KEY,
                    name TEXT NOT NULL,
                    age INTEGER,
                    post TEXT
                );
                """;
        
        // "user" can be problematic as an identifier in some SQL dialects.
        // SQLite usually allows it, but quoting makes it safer and more portable.
        String sqlUsers = """
                CREATE TABLE IF NOT EXISTS "user" (
                    name TEXT NOT NULL,
                    phone TEXT PRIMARY KEY,
                    password TEXT NOT NULL
                );
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlApplicants);
            stmt.execute(sqlUsers);
            
            // Optional: Insert a default user if the table is empty for testing
            String checkUser = "SELECT count(*) FROM \"user\"";
            ResultSet rs = stmt.executeQuery(checkUser);
            if (rs.next() && rs.getInt(1) == 0) {
                String defaultUser = "INSERT INTO \"user\"(name, phone, password) VALUES('Admin', '12345', 'password')";
                stmt.execute(defaultUser);
            }
            
            System.out.println("DB Path: " + new File(DB_FILE).getAbsolutePath());
            System.out.println("Tables checked/created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // New method for Authentication
    public boolean authenticate(String phone, String password) {
        String sql = "SELECT 1 FROM \"user\" WHERE phone = ? AND password = ? LIMIT 1";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone == null ? "" : phone.trim());
            pstmt.setString(2, password == null ? "" : password.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if a match is found
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    // Insert applicant
    public void insertApplicant(int id, String name, int age, String post) {
        String sql = "INSERT INTO applicants(id, name, age, post) VALUES(?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);
            pstmt.setString(4, post);
            pstmt.executeUpdate();
        } catch (SQLException e) { System.out.println(e.getMessage()); }
    }
    // Read all applicants
    public List<String[]> getApplicantsList() {
        String sql = "SELECT * FROM applicants";
        List<String[]> list = new ArrayList<>();
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new String[]{String.valueOf(rs.getInt("id")), rs.getString("name"), String.valueOf(rs.getInt("age")), rs.getString("post")});
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return list;
    }
    // Delete applicants
    public void deleteApplicant(int id) {
        String sql = "DELETE FROM applicants WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { System.out.println(e.getMessage()); }
    }
}