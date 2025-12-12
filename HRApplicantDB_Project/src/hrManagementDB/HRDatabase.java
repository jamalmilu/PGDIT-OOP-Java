package hrManagementDB;

import java.sql.*;

public class HRDatabase {

    private static final String URL = "jdbc:sqlite:hrApplicant.db";

    // Create connection
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Create table
    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS applicants (
                    id INTEGER PRIMARY KEY,
                    name TEXT NOT NULL,
                    age INTEGER,
                    post TEXT
                );
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table applicants created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Insert applicant
    public void insertApplicant(int id, String name, int age, String post) {
        String sql = "INSERT INTO applicants(id, name, age, post) VALUES(?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);
            pstmt.setString(4, post);
            pstmt.executeUpdate();

            System.out.println("applicants added. Name: "+ name);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Read all applicants
    public void getApplicants() {
        String sql = "SELECT * FROM applicants";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getInt("age") + " | " +
                        rs.getString("post")
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Update applicants
    public void updateApplicant(int id, String name, int age, String post) {
        String sql = "UPDATE applicants SET name = ?, age = ?, post = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);  // name
            pstmt.setInt(2, age);      // age
            pstmt.setString(3, post);  // post
            pstmt.setInt(4, id);       // id (WHERE)

            pstmt.executeUpdate();
            System.out.println("Applicant updated.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    // Delete applicants
    public void deleteApplicant(int id) {
        String sql = "DELETE FROM applicants WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            System.out.println("applicants deleted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
