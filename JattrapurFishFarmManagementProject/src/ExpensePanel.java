import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class ExpensePanel extends JPanel {
    private DatabaseManager db;
    private JComboBox<String> itemCombo;
    private JTextField qtyField, priceField, dateField;
    private JTable table;

    private static final String[] ITEMS = {
            "Rice Bran", "Fish Feed", "Urea", "Potash", "Lime",
            "Electricity Bill", "Labor Wage", "Other"
    };

    public ExpensePanel(DatabaseManager db) {
        this.db = db;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Expense Management"));

        createInputPanel();
        loadTable();
    }

    private void createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Expense"));

        itemCombo = new JComboBox<>(ITEMS);
        qtyField = new JTextField();
        priceField = new JTextField();

        // Set today's date as default
        dateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        inputPanel.add(new JLabel("Item:"));
        inputPanel.add(itemCombo);
        inputPanel.add(new JLabel("Quantity (kg):"));
        inputPanel.add(qtyField);
        inputPanel.add(new JLabel("Price per kg:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        inputPanel.add(dateField);

        JButton addBtn = new JButton("Add Expense");
        JButton clearBtn = new JButton("Clear");

        addBtn.addActionListener(e -> addExpense());
        clearBtn.addActionListener(e -> clearFields());

        inputPanel.add(addBtn);
        inputPanel.add(clearBtn);

        add(inputPanel, BorderLayout.NORTH);
    }

    private void addExpense() {
        try {
            double qty = Double.parseDouble(qtyField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            String date = dateField.getText().trim();
            double total = qty * price;

            if (qty <= 0 || price <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity and price must be positive!");
                return;
            }

            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Date format must be YYYY-MM-DD!");
                return;
            }

            try (Connection conn = DriverManager.getConnection(DatabaseManager.DB_URL)) {
                String sql = "INSERT INTO Expenses (item_name, quantity, unit_price, total_cost, date) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, (String) itemCombo.getSelectedItem());
                stmt.setDouble(2, qty);
                stmt.setDouble(3, price);
                stmt.setDouble(4, total);
                stmt.setString(5, date);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Expense added!\nTotal: ৳" + total);
                clearFields();
                loadTable();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void clearFields() {
        qtyField.setText("");
        priceField.setText("");
        dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    private void loadTable() {
        try (Connection conn = DriverManager.getConnection(DatabaseManager.DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Expenses ORDER BY id DESC LIMIT 50");

            Vector<Vector<Object>> data = new Vector<>();
            double totalExpense = 0;

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("item_name"));
                row.add(rs.getDouble("quantity"));
                row.add(rs.getDouble("unit_price"));
                row.add(rs.getDouble("total_cost"));
                row.add(rs.getString("date"));
                data.add(row);
                totalExpense += rs.getDouble("total_cost");
            }

            Vector<String> columns = new Vector<>();
            columns.add("ID");
            columns.add("Item");
            columns.add("Quantity");
            columns.add("Unit Price");
            columns.add("Total Cost");
            columns.add("Date");

            if (table != null) {
                remove((Component) table.getParent());
            }

            table = new JTable(data, columns);
            add(new JScrollPane(table), BorderLayout.CENTER);

            // Total display
            JLabel totalLabel = new JLabel("Total Expenses: ৳" + String.format("%.2f", totalExpense), JLabel.CENTER);
            totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
            totalLabel.setForeground(Color.RED);

            // Remove previous total label if exists
            Component[] components = getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel && comp != totalLabel) {
                    remove(comp);
                }
            }

            add(totalLabel, BorderLayout.SOUTH);

            revalidate();
            repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}