import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class StaffPanel extends JPanel {
    private final DatabaseManager db;

    private static final String[] ROLES = {"Fisherman", "Manager", "Guard", "Temporary Fisherman"};

    private final JTextField nameField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JComboBox<String> roleCombo = new JComboBox<>(ROLES);
    private final JTextField salaryField = new JTextField();
    private final JTextField hireDateField = new JTextField();
    private final JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Inactive"});

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID","Name","Phone","Role","Salary","Hire Date","Status"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    private final JTable table = new JTable(model);
    private Integer selectedId = null;

    public StaffPanel(DatabaseManager db) {
        this.db = db;
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createTitledBorder("Staff Management"));

        hireDateField.setText(today());

        UiUtil.styleTable(table);
        UiUtil.applyCurrencyRenderer(table, 4); // Salary

        // Temporary Fishermen: no salary, only sales commission (handled in Profit & Loss)
        roleCombo.addActionListener(e -> enforceRoleRules());
        enforceRoleRules();

        add(buildForm(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = table.getSelectedRow();
            if (row < 0) return;

            selectedId = toInt(model.getValueAt(row, 0));
            nameField.setText(String.valueOf(model.getValueAt(row, 1)));
            Object ph = model.getValueAt(row, 2);
            phoneField.setText(ph == null ? "" : String.valueOf(ph));
            roleCombo.setSelectedItem(String.valueOf(model.getValueAt(row, 3)));
            salaryField.setText(String.valueOf(model.getValueAt(row, 4)));
            hireDateField.setText(String.valueOf(model.getValueAt(row, 5)));
            statusCombo.setSelectedItem(String.valueOf(model.getValueAt(row, 6)));

            enforceRoleRules();
        });

        refresh();
    }

    private void enforceRoleRules() {
        String role = (String) roleCombo.getSelectedItem();
        boolean isTemp = "Temporary Fisherman".equalsIgnoreCase(role);
        salaryField.setEnabled(!isTemp);
        if (isTemp) {
            salaryField.setText("0");
        }
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,6,6,6);
        g.fill = GridBagConstraints.HORIZONTAL;

        int r=0;
        g.gridx=0; g.gridy=r; form.add(new JLabel("Name"), g);
        g.gridx=1; form.add(nameField, g);

        g.gridx=2; form.add(new JLabel("Phone (optional)"), g);
        g.gridx=3; form.add(phoneField, g);

        r++;
        g.gridx=0; g.gridy=r; form.add(new JLabel("Role"), g);
        g.gridx=1; form.add(roleCombo, g);

        g.gridx=2; form.add(new JLabel("Salary"), g);
        g.gridx=3; form.add(salaryField, g);

        r++;
        g.gridx=0; g.gridy=r; form.add(new JLabel("Hire Date (YYYY-MM-DD)"), g);
        g.gridx=1; form.add(hireDateField, g);

        g.gridx=2; form.add(new JLabel("Status"), g);
        g.gridx=3; form.add(statusCombo, g);

        r++;
        JButton save = new JButton("Save");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton clear = new JButton("Clear");

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.add(clear);
        btns.add(delete);
        btns.add(update);
        btns.add(save);

        g.gridx=0; g.gridy=r; g.gridwidth=4;
        form.add(btns, g);

        save.addActionListener(e -> save());
        update.addActionListener(e -> update());
        delete.addActionListener(e -> delete());
        clear.addActionListener(e -> clearForm());

        return form;
    }

    private void save() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.");
            return;
        }

        String phone = phoneField.getText().trim();
        String role = (String) roleCombo.getSelectedItem();
        double salary;
        if ("Temporary Fisherman".equalsIgnoreCase(role)) {
            salary = 0.0;
        } else {
            Double s = parsePositive(salaryField.getText(), "Salary");
            if (s == null) return;
            salary = s;
        }

        String hireDate = hireDateField.getText().trim();
        String status = (String) statusCombo.getSelectedItem();

        int rows = db.executeUpdate(
                "INSERT INTO Staff(name, phone, role, salary, hire_date, status) VALUES(?,?,?,?,?,?)",
                name, phone.isEmpty()? null : phone, role, salary, hireDate, status
        );

        if (rows == 1) {
            clearForm();
            refresh();
        }
    }

    private void update() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Select a row first.");
            return;
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.");
            return;
        }

        String phone = phoneField.getText().trim();
        String role = (String) roleCombo.getSelectedItem();
        double salary;
        if ("Temporary Fisherman".equalsIgnoreCase(role)) {
            salary = 0.0;
        } else {
            Double s = parsePositive(salaryField.getText(), "Salary");
            if (s == null) return;
            salary = s;
        }

        String hireDate = hireDateField.getText().trim();
        String status = (String) statusCombo.getSelectedItem();

        int rows = db.executeUpdate(
                "UPDATE Staff SET name=?, phone=?, role=?, salary=?, hire_date=?, status=? WHERE id=?",
                name, phone.isEmpty()? null : phone, role, salary, hireDate, status, selectedId
        );

        if (rows == 1) {
            clearForm();
            refresh();
        }
    }

    private void delete() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Select a row first.");
            return;
        }
        int c = JOptionPane.showConfirmDialog(this, "Delete selected staff?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;

        int rows = db.executeUpdate("DELETE FROM Staff WHERE id=?", selectedId);
        if (rows == 1) {
            clearForm();
            refresh();
        }
    }

    private void refresh() {
        model.setRowCount(0);
        Vector<Vector<Object>> data = db.queryTable(
                "SELECT id, name, phone, role, salary, hire_date, status FROM Staff ORDER BY id DESC"
        );
        for (Vector<Object> row : data) model.addRow(row);
    }

    private void clearForm() {
        selectedId = null;
        nameField.setText("");
        phoneField.setText("");
        roleCombo.setSelectedIndex(0);
        salaryField.setText("");
        hireDateField.setText(today());
        statusCombo.setSelectedItem("Active");
        table.clearSelection();
    }

    private Double parsePositive(String s, String field) {
        try {
            double v = Double.parseDouble(s.trim());
            if (v <= 0) throw new NumberFormatException();
            return v;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, field + " must be a positive number.");
            return null;
        }
    }

    private Integer toInt(Object o) {
        if (o == null) return null;
        if (o instanceof Number n) return n.intValue();
        try { return Integer.parseInt(o.toString()); } catch (Exception e) { return null; }
    }

    private String today() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
