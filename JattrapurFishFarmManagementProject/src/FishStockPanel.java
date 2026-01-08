import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FishStockPanel extends JPanel {
    private DatabaseManager db;
    private JComboBox<String> fishTypeCombo;
    private JTextField qtyField, harvestField;
    private JTable table;

    public FishStockPanel(DatabaseManager db) {
        this.db = db;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Fish Stock Management"));

        createInputPanel();
        loadTable();
    }

    private void createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Fish Stock"));

        fishTypeCombo = new JComboBox<>(new String[]{"Rui", "Katla", "Mrigel", "Silver Carp"});
        qtyField = new JTextField();

        // Set default harvest date (2 months from today)
        LocalDate harvestDate = LocalDate.now().plusMonths(2);
        harvestField = new JTextField(harvestDate.format(DateTimeFormatter.ISO_DATE));

        inputPanel.add(new JLabel("Fish Type:"));
        inputPanel.add(fishTypeCombo);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(qtyField);
        inputPanel.add(new JLabel("Harvest Date (YYYY-MM-DD):"));
        inputPanel.add(harvestField);

        JButton addBtn = new JButton("Add Fish");
        addBtn.addActionListener(e -> addFish());
        inputPanel.add(new JLabel());
        inputPanel.add(addBtn);

        add(inputPanel, BorderLayout.NORTH);
    }

    private void addFish() {
        try {
            String fishType = (String) fishTypeCombo.getSelectedItem();
            int quantity = Integer.parseInt(qtyField.getText().trim());
            String harvestDate = harvestField.getText().trim();

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be positive!");
                return;
            }

            if (!harvestDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Date format must be YYYY-MM-DD!");
                return;
            }

            String sql = "INSERT INTO FishStock (fish_type, quantity, purchase_date, expected_harvest, status) VALUES (?, ?, DATE('now'), ?, 'Active')";

            try (Connection conn = DriverManager.getConnection(DatabaseManager.DB_URL);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, fishType);
                stmt.setInt(2, quantity);
                stmt.setString(3, harvestDate);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Fish added successfully!");
                qtyField.setText("");
                loadTable();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid quantity!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void loadTable() {
        try (Connection conn = DriverManager.getConnection(DatabaseManager.DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM FishStock ORDER BY id DESC");

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("fish_type"));
                row.add(rs.getInt("quantity"));
                row.add(rs.getString("purchase_date"));
                row.add(rs.getString("expected_harvest"));
                row.add(rs.getString("status"));
                data.add(row);
            }

            Vector<String> columns = new Vector<>();
            columns.add("ID");
            columns.add("Fish Type");
            columns.add("Quantity");
            columns.add("Purchase Date");
            columns.add("Expected Harvest");
            columns.add("Status");

            if (table != null) {
                remove((Component) table.getParent());
            }

            table = new JTable(data, columns);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            revalidate();
            repaint();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading table: " + e.getMessage());
        }
    }
}