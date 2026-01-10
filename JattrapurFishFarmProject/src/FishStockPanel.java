import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class FishStockPanel extends JPanel {
    private final DatabaseManager db;

    private static final String[] FISH_TYPES = {"Rui", "Katla", "Mrigel", "Silver Carp"};
    private final JComboBox<String> fishTypeCombo = new JComboBox<>(FISH_TYPES);

    private final JTextField qtyKgField = new JTextField();
    private final JTextField pricePerKgField = new JTextField();
    private final JTextField purchaseDateField = new JTextField();
    private final JTextField expectedHarvestField = new JTextField();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID","Fish Type","Quantity (kg)","Price/kg","Total Cost","Purchase Date","Expected Harvest"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    private final JTable table = new JTable(model);
    private Integer selectedId = null;

    public FishStockPanel(DatabaseManager db) {
        this.db = db;
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createTitledBorder("Fish Stock Management"));

        purchaseDateField.setText(today());

        UiUtil.styleTable(table);
        UiUtil.applyNumberRenderer(table, 2);      // Quantity (kg)
        UiUtil.applyCurrencyRenderer(table, 3, 4); // Price/kg, Total Cost

        add(buildForm(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = table.getSelectedRow();
            if (row < 0) return;

            selectedId = toInt(model.getValueAt(row, 0));
            fishTypeCombo.setSelectedItem(String.valueOf(model.getValueAt(row, 1)));
            qtyKgField.setText(String.valueOf(model.getValueAt(row, 2)));
            pricePerKgField.setText(String.valueOf(model.getValueAt(row, 3)));
            purchaseDateField.setText(String.valueOf(model.getValueAt(row, 5)));
            Object eh = model.getValueAt(row, 6);
            expectedHarvestField.setText(eh == null ? "" : String.valueOf(eh));
        });

        refresh();
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,6,6,6);
        g.fill = GridBagConstraints.HORIZONTAL;

        int r=0;
        g.gridx=0; g.gridy=r; form.add(new JLabel("Fish Type"), g);
        g.gridx=1; form.add(fishTypeCombo, g);

        g.gridx=2; form.add(new JLabel("Quantity (kg)"), g);
        g.gridx=3; form.add(qtyKgField, g);

        r++;
        g.gridx=0; g.gridy=r; form.add(new JLabel("Price per kg"), g);
        g.gridx=1; form.add(pricePerKgField, g);

        g.gridx=2; form.add(new JLabel("Purchase Date (YYYY-MM-DD)"), g);
        g.gridx=3; form.add(purchaseDateField, g);

        r++;
        g.gridx=0; g.gridy=r; form.add(new JLabel("Expected Harvest (optional)"), g);
        g.gridx=1; g.gridwidth=3; form.add(expectedHarvestField, g);
        g.gridwidth=1;

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
        String fishType = (String) fishTypeCombo.getSelectedItem();
        Double qtyKg = parsePositive(qtyKgField.getText(), "Quantity (kg)");
        Double priceKg = parsePositive(pricePerKgField.getText(), "Price per kg");
        if (qtyKg == null || priceKg == null) return;

        double total = qtyKg * priceKg;
        String pDate = purchaseDateField.getText().trim();
        String exp = expectedHarvestField.getText().trim();

        int rows = db.executeUpdate(
                "INSERT INTO FishStock(fish_type,quantity_kg,price_per_kg,total_cost,purchase_date,expected_harvest_date) VALUES(?,?,?,?,?,?)",
                fishType, qtyKg, priceKg, total, pDate, exp.isEmpty()? null : exp
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

        String fishType = (String) fishTypeCombo.getSelectedItem();
        Double qtyKg = parsePositive(qtyKgField.getText(), "Quantity (kg)");
        Double priceKg = parsePositive(pricePerKgField.getText(), "Price per kg");
        if (qtyKg == null || priceKg == null) return;

        double total = qtyKg * priceKg;
        String pDate = purchaseDateField.getText().trim();
        String exp = expectedHarvestField.getText().trim();

        int rows = db.executeUpdate(
                "UPDATE FishStock SET fish_type=?, quantity_kg=?, price_per_kg=?, total_cost=?, purchase_date=?, expected_harvest_date=? WHERE id=?",
                fishType, qtyKg, priceKg, total, pDate, exp.isEmpty()? null : exp, selectedId
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

        int rows = db.executeUpdate("DELETE FROM FishStock WHERE id=?", selectedId);
        if (rows == 1) {
            clearForm();
            refresh();
        }
    }

    private void refresh() {
        model.setRowCount(0);
        Vector<Vector<Object>> data = db.queryTable(
                "SELECT id, fish_type, quantity_kg, price_per_kg, total_cost, purchase_date, expected_harvest_date FROM FishStock ORDER BY id DESC"
        );
        for (Vector<Object> row : data) model.addRow(row);
    }

    private void clearForm() {
        selectedId = null;
        fishTypeCombo.setSelectedIndex(0);
        qtyKgField.setText("");
        pricePerKgField.setText("");
        purchaseDateField.setText(today());
        expectedHarvestField.setText("");
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
