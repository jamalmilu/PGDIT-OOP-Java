import javax.swing.*;
import java.sql.*;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

/**
 * SQLite helper with basic schema creation + lightweight "upgrade" to avoid crashes
 * if you ran an older version of the app before.
 *
 * NOTE: SQLite cannot DROP columns easily; we only ADD missing columns.
 */
public class DatabaseManager {
    public static final String DB_URL = "jdbc:sqlite:fish_farm.db";

    private static DatabaseManager instance;
    private final ReentrantLock lock = new ReentrantLock();

    private DatabaseManager() {
        init();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private void init() {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {

            st.execute("""
                CREATE TABLE IF NOT EXISTS Users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    phone TEXT NOT NULL UNIQUE,
                    password_hash TEXT NOT NULL,
                    role TEXT NOT NULL DEFAULT 'USER',
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS FishStock (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    fish_type TEXT NOT NULL CHECK (fish_type IN ('Rui','Katla','Mrigel','Silver Carp')),
                    quantity_kg REAL NOT NULL,
                    price_per_kg REAL NOT NULL,
                    total_cost REAL NOT NULL,
                    purchase_date TEXT NOT NULL,
                    expected_harvest_date TEXT
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS Expenses (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    item TEXT NOT NULL,
                    quantity REAL NOT NULL,
                    unit_price REAL NOT NULL,
                    total_cost REAL NOT NULL,
                    date TEXT NOT NULL
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS Sales (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    fish_type TEXT NOT NULL CHECK (fish_type IN ('Rui','Katla','Mrigel','Silver Carp')),
                    quantity_kg REAL NOT NULL,
                    price_per_kg REAL NOT NULL,
                    total_amount REAL NOT NULL,
                    sale_date TEXT NOT NULL,
                    buyer_name TEXT
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS Staff (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    phone TEXT,
                    role TEXT NOT NULL CHECK (role IN ('Fisherman','Manager','Guard','Temporary Fisherman')),
                    salary REAL NOT NULL,
                    status TEXT NOT NULL DEFAULT 'Active',
                    join_date TEXT DEFAULT CURRENT_TIMESTAMP
                );
            """);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "DB Init Error: " + e.getMessage());
            return;
        }

        // Lightweight upgrades for older DBs (only ADD columns if missing).
        try (Connection conn = getConnection()) {
            // FishStock older versions might have quantity/unit_price columns
            ensureColumn(conn, "FishStock", "quantity_kg", "REAL");
            ensureColumn(conn, "FishStock", "price_per_kg", "REAL");
            ensureColumn(conn, "FishStock", "total_cost", "REAL");
            ensureColumn(conn, "FishStock", "purchase_date", "TEXT");
            ensureColumn(conn, "FishStock", "expected_harvest_date", "TEXT");

            ensureColumn(conn, "Sales", "quantity_kg", "REAL");
            ensureColumn(conn, "Sales", "price_per_kg", "REAL");
            ensureColumn(conn, "Sales", "total_amount", "REAL");
            ensureColumn(conn, "Sales", "sale_date", "TEXT");
            ensureColumn(conn, "Sales", "buyer_name", "TEXT");

            ensureColumn(conn, "Staff", "join_date", "TEXT");
            ensureColumn(conn, "Staff", "status", "TEXT");

        } catch (SQLException ignored) {
            // If upgrade fails, app may still run on a fresh DB; errors will show in panels.
        }
    }

    private void ensureColumn(Connection conn, String table, String column, String type) throws SQLException {
        if (!tableExists(conn, table)) return;
        if (columnExists(conn, table, column)) return;
        try (Statement st = conn.createStatement()) {
            st.execute("ALTER TABLE " + table + " ADD COLUMN " + column + " " + type);
        }
    }

    private boolean tableExists(Connection conn, String table) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?")) {
            ps.setString(1, table);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean columnExists(Connection conn, String table, String column) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("PRAGMA table_info(" + table + ")")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String colName = rs.getString("name");
                    if (column.equalsIgnoreCase(colName)) return true;
                }
                return false;
            }
        }
    }

    public int executeUpdate(String sql, Object... params) {
        lock.lock();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bind(ps, params);
            return ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "DB Update Error: " + e.getMessage());
            return 0;
        } finally {
            lock.unlock();
        }
    }

    public Object[] querySingleRow(String sql, Object... params) {
        lock.lock();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            bind(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();
                if (!rs.next()) return null;

                Object[] row = new Object[cols];
                for (int i = 1; i <= cols; i++) row[i - 1] = rs.getObject(i);
                return row;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "DB Query Error: " + e.getMessage());
            return null;
        } finally {
            lock.unlock();
        }
    }

    public Vector<Vector<Object>> queryTable(String sql, Object... params) {
        lock.lock();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            bind(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                Vector<Vector<Object>> data = new Vector<>();
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();

                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    for (int i = 1; i <= cols; i++) row.add(rs.getObject(i));
                    data.add(row);
                }
                return data;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "DB Query Error: " + e.getMessage());
            return new Vector<>();
        } finally {
            lock.unlock();
        }
    }

    private void bind(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
    }
}
