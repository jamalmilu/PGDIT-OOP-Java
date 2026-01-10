import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Single DB access class (SQLite).
 * Default package to match your current project structure.
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

    /** Create tables + add missing columns if the DB already exists. */
    private void init() {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {

            st.execute("""
                CREATE TABLE IF NOT EXISTS Users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    full_name TEXT NOT NULL,
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
                    item_name TEXT NOT NULL,
                    quantity REAL NOT NULL,
                    unit_price REAL NOT NULL,
                    total_cost REAL NOT NULL,
                    date TEXT NOT NULL
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS Staff (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    phone TEXT,
                    role TEXT NOT NULL CHECK (role IN ('Fisherman','Manager','Guard','Temporary Fisherman')),
                    salary REAL NOT NULL,
                    hire_date TEXT NOT NULL,
                    status TEXT NOT NULL DEFAULT 'Active'
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

            // If DB existed with old schemas, add missing columns (safe no-op if already present).
            ensureColumn(conn, "Users", "full_name", "TEXT");
            ensureColumn(conn, "Users", "password_hash", "TEXT");
            ensureColumn(conn, "Users", "role", "TEXT");
            ensureColumn(conn, "FishStock", "quantity_kg", "REAL");
            ensureColumn(conn, "FishStock", "price_per_kg", "REAL");
            ensureColumn(conn, "FishStock", "total_cost", "REAL");
            ensureColumn(conn, "Expenses", "item_name", "TEXT");
            ensureColumn(conn, "Expenses", "quantity", "REAL");
            ensureColumn(conn, "Expenses", "unit_price", "REAL");
            ensureColumn(conn, "Expenses", "total_cost", "REAL");
            ensureColumn(conn, "Staff", "hire_date", "TEXT");
            ensureColumn(conn, "Staff", "status", "TEXT");
            ensureColumn(conn, "Sales", "total_amount", "REAL");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "DB init error: " + e.getMessage());
        }
    }

    private void ensureColumn(Connection conn, String table, String column, String type) {
        try {
            if (hasColumn(conn, table, column)) return;
            try (Statement st = conn.createStatement()) {
                st.execute("ALTER TABLE " + table + " ADD COLUMN " + column + " " + type);
            }
        } catch (SQLException ignored) {
            // Old DB may have constraints preventing alter; user can delete DB if needed.
        }
    }

    private boolean hasColumn(Connection conn, String table, String column) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("PRAGMA table_info(" + table + ")");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("name");
                if (column.equalsIgnoreCase(name)) return true;
            }
        }
        return false;
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

    // ----------------------------
    // Profit / Loss (Ledger style)
    // ----------------------------

    public static final class LedgerEntry {
        public final String account;
        public final double amount;

        public LedgerEntry(String account, double amount) {
            this.account = account;
            this.amount = amount;
        }
    }

    /**
     * A T-account style ledger for a period.
     * Debits = costs/expenses. Credits = sales/revenue.
     * Includes balancing entry (Net Income or Net Loss) so totals match.
     */
    public static final class ProfitLossLedger {
        public final LocalDate start;
        public final LocalDate end;
        public final List<LedgerEntry> debits;
        public final List<LedgerEntry> credits;
        public final double totalDebits;
        public final double totalCredits;
        public final double netIncome; // +profit, -loss
        public final double salesRevenue;
        public final int regularStaffCount;
        public final int tempFishermenCount;

        private ProfitLossLedger(
                LocalDate start,
                LocalDate end,
                List<LedgerEntry> debits,
                List<LedgerEntry> credits,
                double totalDebits,
                double totalCredits,
                double netIncome,
                double salesRevenue,
                int regularStaffCount,
                int tempFishermenCount
        ) {
            this.start = start;
            this.end = end;
            this.debits = debits;
            this.credits = credits;
            this.totalDebits = totalDebits;
            this.totalCredits = totalCredits;
            this.netIncome = netIncome;
            this.salesRevenue = salesRevenue;
            this.regularStaffCount = regularStaffCount;
            this.tempFishermenCount = tempFishermenCount;
        }
    }

    /**
     * Build a detailed Profit & Loss ledger for a date range (inclusive).
     *
     * Rules:
     *  - Regular staff (all roles except "Temporary Fisherman") are salaried monthly, prorated by days.
     *  - Temporary Fishermen have no salary.
     *  - If there is at least one Active Temporary Fisherman, they get 14% commission on Sales in the period.
     */
    public ProfitLossLedger getProfitLossLedger(LocalDate start, LocalDate end) throws SQLException {
        lock.lock();
        try (Connection conn = getConnection()) {
            List<LedgerEntry> debits = new ArrayList<>();
            List<LedgerEntry> credits = new ArrayList<>();

            // -------- Credits: Sales (line-by-line)
            double salesRevenue = 0.0;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, fish_type, quantity_kg, price_per_kg, total_amount, sale_date, buyer_name " +
                            "FROM Sales WHERE sale_date BETWEEN ? AND ? ORDER BY sale_date ASC, id ASC"
            )) {
                ps.setString(1, start.toString());
                ps.setString(2, end.toString());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String fishType = rs.getString("fish_type");
                        double qty = rs.getDouble("quantity_kg");
                        double price = rs.getDouble("price_per_kg");
                        double total = rs.getDouble("total_amount");
                        String date = rs.getString("sale_date");
                        String buyer = rs.getString("buyer_name");
                        salesRevenue += total;

                        String label = "Sales: " + fishType + " | " + qty + " kg @ " + price + " | " + date +
                                (buyer == null || buyer.isBlank() ? "" : " | Buyer: " + buyer) +
                                " | #" + id;
                        credits.add(new LedgerEntry(label, total));
                    }
                }
            }

            // -------- Debits: Operating expenses (line-by-line)
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, item_name, quantity, unit_price, total_cost, date " +
                            "FROM Expenses WHERE date BETWEEN ? AND ? ORDER BY date ASC, id ASC"
            )) {
                ps.setString(1, start.toString());
                ps.setString(2, end.toString());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String item = rs.getString("item_name");
                        double qty = rs.getDouble("quantity");
                        double unit = rs.getDouble("unit_price");
                        double total = rs.getDouble("total_cost");
                        String date = rs.getString("date");

                        String label = "Expense: " + item + " | " + qty + " x " + unit + " | " + date + " | #" + id;
                        debits.add(new LedgerEntry(label, total));
                    }
                }
            }

            // -------- Debits: Fish purchase cost (line-by-line)
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, fish_type, quantity_kg, price_per_kg, total_cost, purchase_date " +
                            "FROM FishStock WHERE purchase_date BETWEEN ? AND ? ORDER BY purchase_date ASC, id ASC"
            )) {
                ps.setString(1, start.toString());
                ps.setString(2, end.toString());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String fishType = rs.getString("fish_type");
                        double qty = rs.getDouble("quantity_kg");
                        double price = rs.getDouble("price_per_kg");
                        double total = rs.getDouble("total_cost");
                        String date = rs.getString("purchase_date");

                        String label = "Fish Purchase: " + fishType + " | " + qty + " kg @ " + price + " | " + date + " | #" + id;
                        debits.add(new LedgerEntry(label, total));
                    }
                }
            }

            // -------- Staff counts
            int tempCount = 0;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(*) AS c FROM Staff WHERE status='Active' AND role='Temporary Fisherman'"
            ); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) tempCount = rs.getInt("c");
            }

            int regularCount = 0;
            // -------- Debits: Salaries (one line per staff, prorated)
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, name, role, salary, hire_date " +
                            "FROM Staff WHERE status='Active' AND role <> 'Temporary Fisherman' ORDER BY id ASC"
            )) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        regularCount++;
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        String role = rs.getString("role");
                        double monthlySalary = rs.getDouble("salary");
                        String hireDateStr = rs.getString("hire_date");
                        LocalDate hireDate;
                        try {
                            hireDate = LocalDate.parse(hireDateStr);
                        } catch (Exception e) {
                            // Bad hire_date format => treat as started before period
                            hireDate = start;
                        }

                        double cost = proratedMonthlySalary(monthlySalary, hireDate, start, end);
                        if (cost > 0) {
                            String label = "Salary: " + name + " (" + role + ") | Hired: " + hireDate + " | #" + id;
                            debits.add(new LedgerEntry(label, cost));
                        }
                    }
                }
            }

            // -------- Debits: Temporary Fishermen commission (14% of sales)
            if (tempCount > 0 && salesRevenue > 0) {
                double commission = salesRevenue * 0.14;
                debits.add(new LedgerEntry("Temp Fishermen Commission (14% of Sales)", commission));
            }

            // -------- Totals (before balancing)
            double debitSum = 0.0;
            for (LedgerEntry e : debits) debitSum += e.amount;
            double creditSum = 0.0;
            for (LedgerEntry e : credits) creditSum += e.amount;

            double netIncome = creditSum - debitSum; // +profit, -loss

            // -------- Balancing entry so totals match (T-format)
            if (netIncome > 0) {
                // Profit: put Net Income on the Debit side to balance credits
                debits.add(new LedgerEntry("Net Income (Profit)", netIncome));
                debitSum += netIncome;
            } else if (netIncome < 0) {
                // Loss: put Net Loss on the Credit side to balance debits
                double loss = -netIncome;
                credits.add(new LedgerEntry("Net Loss", loss));
                creditSum += loss;
            }

            return new ProfitLossLedger(start, end, debits, credits, debitSum, creditSum, netIncome, salesRevenue, regularCount, tempCount);
        } finally {
            lock.unlock();
        }
    }

    private double proratedMonthlySalary(double monthlySalary, LocalDate hireDate, LocalDate start, LocalDate end) {
        if (monthlySalary <= 0) return 0.0;
        LocalDate effStart = start;
        if (hireDate != null && hireDate.isAfter(effStart)) effStart = hireDate;
        if (effStart.isAfter(end)) return 0.0;

        double total = 0.0;
        LocalDate cursor = effStart;
        while (!cursor.isAfter(end)) {
            YearMonth ym = YearMonth.from(cursor);
            LocalDate monthStart = ym.atDay(1);
            LocalDate monthEnd = ym.atEndOfMonth();

            LocalDate segStart = cursor;
            LocalDate segEnd = monthEnd.isBefore(end) ? monthEnd : end;

            long daysInMonth = ChronoUnit.DAYS.between(monthStart, monthEnd) + 1;
            long daysWorked = ChronoUnit.DAYS.between(segStart, segEnd) + 1;
            if (daysInMonth > 0 && daysWorked > 0) {
                total += monthlySalary * (daysWorked / (double) daysInMonth);
            }

            cursor = segEnd.plusDays(1);
        }
        return total;
    }
}
