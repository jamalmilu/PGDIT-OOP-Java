import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class ExpensePanel extends JPanel {
    private final DatabaseManager db;

    private static final String[] ITEMS = {
            "Khoil", "Fish Feed", "Urea", "Potash", "Lime",
            "Electricity Bill", "Labor Wage", "Transport", "Repair", "Other"
    };

    private final JComboBox<String> itemCombo = new JComboBox<>(ITEMS);
    private final JTextField qtyField = new JTextField();
    private final JTextField priceField = new JTextField();
    private final JTextField dateField = new JTextField();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID","Item","Quantity","Unit Price","Total Cost","Date"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    private final JTable table = new JTable(model);
    private Integer selectedId = null;

    public ExpensePanel(DatabaseManager db) {
        this.db = db;
        setLayout(new BorderLayout(10,10));

        dateField.setText(today());

        add(buildForm(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = table.getSelectedRow();
            if (row < 0) return;

            selectedId = (Integer) model.getValueAt(row, 0);
            itemCombo.setSelectedItem(model.getValueAt(row, 1).toString());
            qtyField.setText(model.getValueAt(row, 2).toString());
            priceField.setText(model.getValueAt(row, 3).toString());
            dateField.setText(model.getValueAt(row, 5).toString());
        });

        refresh();
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Expense Entry"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,6,6,6);
        g.fill = GridBagConstraints.HORIZONTAL;

        int r=0;
        g.gridx=0; g.gridy=r; form.add(new JLabel("Item"), g);
        g.gridx=1; form.add(itemCombo, g);

        g.gridx=2; form.add(new JLabel("Quantity"), g);
        g.gridx=3; form.add(qtyField, g);

        r++;
        g.gridx=0; g.gridy=r; form.add(new JLabel("Unit Price"), g);
        g.gridx=1; form.add(priceField, g);

        g.gridx=2; form.add(new JLabel("Date (YYYY-MM-DD)"), g);
        g.gridx=3; form.add(dateField, g);

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
        String item = (String) itemCombo.getSelectedItem();
        Double qty = parseDouble(qtyField.getText(), "Quantity");
        Double price = parseDouble(priceField.getText(), "Unit price");
        if (qty == null || price == null) return;

        String date = dateField.getText().trim();
        if (!isValidDate(date)) {
            JOptionPane.showMessageDialog(this, "Date must be YYYY-MM-DD.");
            return;
        }

        double total = qty * price;

        int rows = db.executeUpdate(
                "INSERT INTO Expenses(item, quantity, unit_price, total_cost, date) VALUES(?,?,?,?,?)",
                item, qty, price, total, date
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

        String item = (String) itemCombo.getSelectedItem();
        Double qty = parseDouble(qtyField.getText(), "Quantity");
        Double price = parseDouble(priceField.getText(), "Unit price");
        if (qty == null || price == null) return;

        String date = dateField.getText().trim();
        if (!isValidDate(date)) {
            JOptionPane.showMessageDialog(this, "Date must be YYYY-MM-DD.");
            return;
        }

        double total = qty * price;

        int rows = db.executeUpdate(
                "UPDATE Expenses SET item=?, quantity=?, unit_price=?, total_cost=?, date=? WHERE id=?",
                item, qty, price, total, date, selectedId
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
        int c = JOptionPane.showConfirmDialog(this, "Delete selected record?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;

        int rows = db.executeUpdate("DELETE FROM Expenses WHERE id=?", selectedId);
        if (rows == 1) {
            clearForm();
            refresh();
        }
    }

    private void refresh() {
        model.setRowCount(0);
        Vector<Vector<Object>> data = db.queryTable(
                "SELECT id, item, quantity, unit_price, total_cost, date FROM Expenses ORDER BY id DESC"
        );
        for (Vector<Object> row : data) model.addRow(row);
    }

    private void clearForm() {
        selectedId = null;
        itemCombo.setSelectedIndex(0);
        qtyField.setText("");
        priceField.setText("");
        dateField.setText(today());
        table.clearSelection();
    }

    private Double parseDouble(String s, String field) {
        try {
            double v = Double.parseDouble(s.trim());
            if (v <= 0) throw new NumberFormatException();
            return v;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, field + " must be a positive number.");
            return null;
        }
    }

    private boolean isValidDate(String s) {
        return s != null && s.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private String today() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
