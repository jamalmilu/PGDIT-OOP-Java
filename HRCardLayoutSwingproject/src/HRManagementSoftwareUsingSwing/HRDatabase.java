package HRManagementSoftwareUsingSwing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HRDatabase {

    private static final String URL = "jdbc:sqlite:hrApplicant.db";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean insertApplicant(int id, String name, int age, String post) throws SQLException {
        String sql = "INSERT INTO applicants(id, name, age, post) VALUES(?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);
            pstmt.setString(4, post);

            int rows = pstmt.executeUpdate();
            return rows == 1;
        }
    }

    public Applicant getApplicantById(int id) throws SQLException {
        String sql = "SELECT id, name, age, post FROM applicants WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Applicant(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("post")
                    );
                }
            }
        }
        return null;
    }

    public List<Applicant> getAllApplicants() throws SQLException {
        String sql = "SELECT id, name, age, post FROM applicants ORDER BY id";
        List<Applicant> list = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Applicant(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("post")
                ));
            }
        }
        return list;
    }

    public boolean updateApplicant(int id, String name, int age, String post) throws SQLException {
        String sql = "UPDATE applicants SET name = ?, age = ?, post = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, post);
            pstmt.setInt(4, id);

            int rows = pstmt.executeUpdate();
            return rows == 1;
        }
    }

    public boolean deleteApplicant(int id) throws SQLException {
        String sql = "DELETE FROM applicants WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            return rows == 1;
        }
    }
}
