import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class StaffPanel extends JPanel {
    private DatabaseManager db;
    private JTextField nameField, phoneField, salaryField, hireDateField;
    private JComboBox<String> roleCombo;
    private JTable table;

    private static final String[] ROLES = {
            "Permanent Fisherman", "Manager", "Guard", "Temporary Fisherman"
    };

    public StaffPanel(DatabaseManager db) {
        this.db = db;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Staff Management"));

        createInputPanel();
        loadTable();
    }

    private void createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Staff"));

        nameField = new JTextField();
        phoneField = new JTextField();
        salaryField = new JTextField();
        roleCombo = new JComboBox<>(ROLES);

        // Set today's date as default
        hireDateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Salary:"));
        inputPanel.add(salaryField);
        inputPanel.add(new JLabel("Role:"));
        inputPanel.add(roleCombo);
        inputPanel.add(new JLabel("Hire Date (YYYY-MM-DD):"));
        inputPanel.add(hireDateField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Staff");
        JButton deleteBtn = new JButton("Deactivate");

        addBtn.addActionListener(e -> addStaff());
        deleteBtn.addActionListener(e -> deleteStaff());

        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);

        inputPanel.add(buttonPanel);
        inputPanel.add(new JLabel());

        add(inputPanel, BorderLayout.NORTH);
    }

    private void addStaff() {
        try {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            double salary = Double.parseDouble(salaryField.getText().trim());
            String role = (String) roleCombo.getSelectedItem();
            String hireDate = hireDateField.getText().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter name and phone!");
                return;
            }

            if (salary <= 0) {
                JOptionPane.showMessageDialog(this, "Salary must be positive!");
                return;
            }

            if (!hireDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Date format must be YYYY-MM-DD!");
                return;
            }

            try (Connection conn = DriverManager.getConnection(DatabaseManager.DB_URL)) {
                String sql = "INSERT INTO Staff (name, phone, role, salary, hire_date, status) VALUES (?, ?, ?, ?, ?, 'Active')";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, name);
                stmt.setString(2, phone);
                stmt.setString(3, role);
                stmt.setDouble(4, salary);
                stmt.setString(5, hireDate);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Staff added!\nSalary: ৳" + salary);
                clearFields();
                loadTable();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid salary amount!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteStaff() {
        if (table == null) {
            JOptionPane.showMessageDialog(this, "No staff records available!");
            return;
        }

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a staff member!");
            return;
        }

        int id = (Integer) table.getValueAt(row, 0);
        String name = (String) table.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deactivate staff member: " + name + "?",
                "Confirm Deactivation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DriverManager.getConnection(DatabaseManager.DB_URL)) {
                String sql = "UPDATE Staff SET status='Inactive' WHERE id=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Staff member deactivated!");
                loadTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        salaryField.setText("");
        hireDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    private void loadTable() {
        try (Connection conn = DriverManager.getConnection(DatabaseManager.DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Staff WHERE status='Active' ORDER BY id DESC");

            Vector<Vector<Object>> data = new Vector<>();
            int activeCount = 0;

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("phone"));
                row.add(rs.getString("role"));
                row.add(rs.getDouble("salary"));
                row.add(rs.getString("hire_date"));
                data.add(row);
                activeCount++;
            }

            Vector<String> columns = new Vector<>();
            columns.add("ID");
            columns.add("Name");
            columns.add("Phone");
            columns.add("Role");
            columns.add("Salary");
            columns.add("Hire Date");

            if (table != null) {
                remove((Component) table.getParent());
            }

            table = new JTable(data, columns);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            add(new JScrollPane(table), BorderLayout.CENTER);

            JLabel countLabel = new JLabel("Active Staff: " + activeCount + " persons", JLabel.CENTER);
            countLabel.setFont(new Font("Arial", Font.BOLD, 14));
            countLabel.setForeground(Color.BLUE);

            // Remove previous count label if exists
            Component[] components = getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel && comp != countLabel) {
                    remove(comp);
                }
            }

            add(countLabel, BorderLayout.SOUTH);

            revalidate();
            repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}