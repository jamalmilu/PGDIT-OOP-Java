import java.sql.*;
import javax.swing.JOptionPane;
import java.util.concurrent.locks.ReentrantLock;

public class DatabaseManager {
    static final String DB_URL = "jdbc:sqlite:fish_farm.db";
    private static DatabaseManager instance;
    private ReentrantLock lock = new ReentrantLock();

    // Singleton pattern - only one instance
    private DatabaseManager() {
        initializeDatabase();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeDatabase() {
        // First, create tables with all required columns
        String[] tables = {
                // Users table with all required columns
                "CREATE TABLE IF NOT EXISTS Users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "phone TEXT UNIQUE NOT NULL, " +
                        "password TEXT NOT NULL, " +
                        "full_name TEXT, " +
                        "email TEXT, " +
                        "role TEXT DEFAULT 'user', " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")",

                // FishStock table
                "CREATE TABLE IF NOT EXISTS FishStock (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "fish_type TEXT NOT NULL, " +
                        "quantity INTEGER NOT NULL, " +
                        "purchase_date DATE NOT NULL, " +
                        "expected_harvest DATE NOT NULL, " +
                        "status TEXT DEFAULT 'Active'" +
                        ")",

                // Expenses table
                "CREATE TABLE IF NOT EXISTS Expenses (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "item_name TEXT NOT NULL, " +
                        "quantity REAL NOT NULL, " +
                        "unit_price REAL NOT NULL, " +
                        "total_cost REAL NOT NULL, " +
                        "date DATE NOT NULL" +
                        ")",

                // Staff table
                "CREATE TABLE IF NOT EXISTS Staff (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "phone TEXT, " +
                        "role TEXT NOT NULL, " +
                        "salary REAL NOT NULL, " +
                        "hire_date DATE NOT NULL, " +
                        "status TEXT DEFAULT 'Active'" +
                        ")",

                // Sales table
                "CREATE TABLE IF NOT EXISTS Sales (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "fish_type TEXT NOT NULL, " +
                        "quantity INTEGER NOT NULL, " +
                        "sale_price REAL NOT NULL, " +
                        "total_revenue REAL NOT NULL, " +
                        "sale_date DATE NOT NULL" +
                        ")"
        };

        executeUpdate(tables);

        // Check if we need to add columns to existing tables
        addMissingColumns();

        // Create default admin user
        createDefaultAdmin();
    }

    private void addMissingColumns() {
        // Check for missing columns and add them if needed
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Check if full_name column exists in Users table
            ResultSet rs = stmt.executeQuery("PRAGMA table_info(Users)");
            boolean hasFullName = false;
            boolean hasEmail = false;

            while (rs.next()) {
                String columnName = rs.getString("name");
                if ("full_name".equalsIgnoreCase(columnName)) {
                    hasFullName = true;
                }
                if ("email".equalsIgnoreCase(columnName)) {
                    hasEmail = true;
                }
            }

            // Add missing columns
            if (!hasFullName) {
                stmt.execute("ALTER TABLE Users ADD COLUMN full_name TEXT");
                System.out.println("Added full_name column to Users table");
            }

            if (!hasEmail) {
                stmt.execute("ALTER TABLE Users ADD COLUMN email TEXT");
                System.out.println("Added email column to Users table");
            }

        } catch (SQLException e) {
            System.out.println("Error checking/adding columns: " + e.getMessage());
        }
    }

    private void createDefaultAdmin() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Check if admin already exists
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Users WHERE phone = '01700000000'");
            if (rs.next() && rs.getInt(1) == 0) {
                // Insert default admin
                String adminSql = "INSERT INTO Users (phone, password, full_name, role) " +
                        "VALUES ('01700000000', 'admin123', 'Admin User', 'admin')";
                stmt.executeUpdate(adminSql);
                System.out.println("Default admin user created!");
            }

        } catch (SQLException e) {
            System.out.println("Error creating default admin: " + e.getMessage());
        }
    }

    // Safe execute methods with proper connection handling
    public void executeUpdate(String... sqls) {
        lock.lock();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false); // Transaction
            try (Statement stmt = conn.createStatement()) {
                for (String sql : sqls) {
                    stmt.executeUpdate(sql);
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        lock.lock();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } finally {
            lock.unlock();
        }
    }

    // Safe query with auto-close
    public Object[] querySingleRow(String sql, Object... params) {
        lock.lock();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String phone = rs.getString("phone");
                    String fullName = rs.getString("full_name");
                    String role = rs.getString("role");
                    return new Object[]{id, phone, fullName, role};
                }
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Query Error: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Query Error: " + e.getMessage());
            return null;
        } finally {
            lock.unlock();
        }
    }

    // New method to check if user exists
    public boolean userExists(String phone) {
        lock.lock();
        String sql = "SELECT COUNT(*) FROM Users WHERE phone = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("User exists check error: " + e.getMessage());
            return false;
        } finally {
            lock.unlock();
        }
    }

    // Execute a single SQL statement
    public boolean execute(String sql) {
        lock.lock();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            return stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Execute error: " + e.getMessage());
            return false;
        } finally {
            lock.unlock();
        }
    }
}