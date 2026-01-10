import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Profit & Loss - Tabular "T-format" (Debit vs Credit) for a selected period.
 *
 * Detailed breakdown:
 *  - Sales listed line-by-line on the CREDIT side.
 *  - Expenses listed line-by-line on the DEBIT side.
 *  - Fish purchases listed line-by-line on the DEBIT side.
 *  - Regular staff salaries listed per staff (prorated) on the DEBIT side.
 *  - Temporary fishermen: no salary; if at least one active temp fisherman exists,
 *    14% commission on total sales is added on the DEBIT side.
 *
 * Balancing:
 *  - If CREDIT > DEBIT => Net Income (Profit) is added to DEBIT to balance totals.
 *  - If DEBIT > CREDIT => Net Loss is added to CREDIT to balance totals.
 */
public class ProfitLossPanel extends JPanel {
    private final DatabaseManager db;

    private final JTextField startDateField = new JTextField(12);
    private final JTextField endDateField = new JTextField(12);

    private final DefaultTableModel ledgerModel = new DefaultTableModel(
            // Unicode escapes keep the source compilable even if the compiler isn't using UTF-8.
            new String[]{"Debit Account", "Debit (\u09F3)", "Credit Account", "Credit (\u09F3)"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable ledgerTable = new JTable(ledgerModel);

    private final JLabel netIncomeLabel = new JLabel("\u09F30.00");
    private final JLabel debitTotalLabel = new JLabel("\u09F30.00");
    private final JLabel creditTotalLabel = new JLabel("\u09F30.00");
    private final JLabel noteLabel = new JLabel(" ");

    public ProfitLossPanel(DatabaseManager db) {
        this.db = db;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Profit & Loss (Tabular T-Format)"));

        LocalDate today = LocalDate.now();
        startDateField.setText(today.withDayOfMonth(1).toString());
        endDateField.setText(today.toString());

        add(buildControls(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        // UI polish
        UIUtil.tuneTable(ledgerTable);
        UIUtil.applyMoneyRenderer(ledgerTable, 1, 3);

        calculate();
    }

    private JPanel buildControls() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        JButton thisMonth = new JButton("This Month");
        JButton lastMonth = new JButton("Last Month");
        JButton calculateBtn = new JButton("Calculate");

        int r = 0;
        g.gridx = 0; g.gridy = r; p.add(new JLabel("Start Date (YYYY-MM-DD)"), g);
        g.gridx = 1; g.weightx = 1; p.add(startDateField, g);
        g.weightx = 0;
        g.gridx = 2; p.add(thisMonth, g);

        r++;
        g.gridx = 0; g.gridy = r; p.add(new JLabel("End Date (YYYY-MM-DD)"), g);
        g.gridx = 1; g.weightx = 1; p.add(endDateField, g);
        g.weightx = 0;
        g.gridx = 2; p.add(lastMonth, g);

        r++;
        g.gridx = 0; g.gridy = r; g.gridwidth = 3;
        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        row.add(calculateBtn);
        p.add(row, g);

        thisMonth.addActionListener(e -> setThisMonth());
        lastMonth.addActionListener(e -> setLastMonth());
        calculateBtn.addActionListener(e -> calculate());

        return p;
    }

    private JComponent buildCenter() {
        JScrollPane sp = new JScrollPane(ledgerTable);
        sp.setBorder(BorderFactory.createTitledBorder("Ledger (Debit vs Credit)"));
        return sp;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 10, 6, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        netIncomeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        debitTotalLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        creditTotalLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        int r = 0;
        g.gridx = 0; g.gridy = r; footer.add(new JLabel("Debit Total:"), g);
        g.gridx = 1; footer.add(debitTotalLabel, g);

        g.gridx = 2; footer.add(new JLabel("Credit Total:"), g);
        g.gridx = 3; footer.add(creditTotalLabel, g);

        r++;
        g.gridx = 0; g.gridy = r; footer.add(new JLabel("Net Income (Profit/Loss):"), g);
        g.gridx = 1; g.gridwidth = 3; footer.add(netIncomeLabel, g);
        g.gridwidth = 1;

        r++;
        g.gridx = 0; g.gridy = r; g.gridwidth = 4;
        noteLabel.setForeground(new Color(90, 90, 90));
        footer.add(noteLabel, g);

        return footer;
    }

    private void setThisMonth() {
        LocalDate today = LocalDate.now();
        startDateField.setText(today.withDayOfMonth(1).toString());
        endDateField.setText(today.toString());
        calculate();
    }

    private void setLastMonth() {
        LocalDate today = LocalDate.now();
        LocalDate firstThisMonth = today.withDayOfMonth(1);
        LocalDate lastMonthEnd = firstThisMonth.minusDays(1);
        LocalDate lastMonthStart = lastMonthEnd.withDayOfMonth(1);
        startDateField.setText(lastMonthStart.toString());
        endDateField.setText(lastMonthEnd.toString());
        calculate();
    }

    private static class Line {
        final String account;
        final double amount;
        Line(String a, double v) { account = a; amount = v; }
    }

    private void calculate() {
        LocalDate start;
        LocalDate end;

        try {
            start = LocalDate.parse(startDateField.getText().trim());
            end = LocalDate.parse(endDateField.getText().trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid dates in YYYY-MM-DD format.");
            return;
        }
        if (start.isAfter(end)) {
            JOptionPane.showMessageDialog(this, "Start date must be on or before end date.");
            return;
        }

        // Gather CREDIT lines (Sales)
        List<Line> credits = new ArrayList<>();
        Vector<Vector<Object>> salesRows = db.getSalesBetween(start, end);
        double salesTotal = 0.0;

        for (Vector<Object> row : salesRows) {
            String fish = String.valueOf(row.get(1));
            double qty = toDouble(row.get(2));
            double price = toDouble(row.get(3));
            double total = toDouble(row.get(4));
            String date = String.valueOf(row.get(5));
            String buyer = row.size() > 6 && row.get(6) != null ? String.valueOf(row.get(6)) : "";

            String label = "Sales: " + fish +
                    " (" + fmt2(qty) + "kg x " + fmt2(price) + "/kg) [" + date + "]" +
                    (buyer.isEmpty() ? "" : " - " + buyer);

            credits.add(new Line(label, total));
            salesTotal += total;
        }

        // Gather DEBIT lines (Purchases + Expenses + Salaries + Commission)
        List<Line> debits = new ArrayList<>();

        Vector<Vector<Object>> purchaseRows = db.getFishPurchasesBetween(start, end);
        double purchaseTotal = 0.0;
        for (Vector<Object> row : purchaseRows) {
            String fish = String.valueOf(row.get(1));
            double qty = toDouble(row.get(2));
            double price = toDouble(row.get(3));
            double total = toDouble(row.get(4));
            String date = String.valueOf(row.get(5));

            String label = "Fish Purchase: " + fish +
                    " (" + fmt2(qty) + "kg x " + fmt2(price) + "/kg) [" + date + "]";
            debits.add(new Line(label, total));
            purchaseTotal += total;
        }

        Vector<Vector<Object>> expenseRows = db.getExpensesBetween(start, end);
        double expenseTotal = 0.0;
        for (Vector<Object> row : expenseRows) {
            String item = String.valueOf(row.get(1));
            double qty = toDouble(row.get(2));
            double unit = toDouble(row.get(3));
            double total = toDouble(row.get(4));
            String date = String.valueOf(row.get(5));

            String label = "Expense: " + item +
                    " (" + fmt2(qty) + " x " + fmt2(unit) + ") [" + date + "]";
            debits.add(new Line(label, total));
            expenseTotal += total;
        }

        // Salaries (regular staff only) - per staff (prorated)
        Vector<Vector<Object>> staff = db.getActiveStaff();
        double salaryTotal = 0.0;
        int regularCount = 0;
        int tempCount = 0;

        for (Vector<Object> row : staff) {
            String name = String.valueOf(row.get(1));
            String role = String.valueOf(row.get(2));
            double monthlySalary = toDouble(row.get(3));

            LocalDate hire = null;
            try { hire = LocalDate.parse(String.valueOf(row.get(4))); } catch (Exception ignored) {}

            if ("Temporary Fisherman".equalsIgnoreCase(role)) {
                tempCount++;
                continue;
            }

            double prorated = DatabaseManager.prorateMonthlySalary(monthlySalary, hire, start, end);
            if (prorated > 0) {
                debits.add(new Line("Salary: " + name + " (" + role + ")", prorated));
                salaryTotal += prorated;
                regularCount++;
            }
        }

        // Temp commission (only if active temp fishermen exist)
        double commission = (tempCount > 0) ? (0.14 * salesTotal) : 0.0;
        if (commission > 0) {
            debits.add(new Line("Temp Fishermen Commission (14% of sales)", commission));
        }

        // Compute totals
        double debitSum = sum(debits);
        double creditSum = sum(credits);

        // Balance with Net Income / Net Loss
        double netIncome = salesTotal - (purchaseTotal + expenseTotal + salaryTotal + commission);

        if (creditSum > debitSum) {
            double profit = creditSum - debitSum;
            debits.add(new Line("Net Income (Profit)", profit));
            debitSum += profit;
        } else if (debitSum > creditSum) {
            double loss = debitSum - creditSum;
            credits.add(new Line("Net Loss", loss));
            creditSum += loss;
        }

        // Render paired T-format rows
        ledgerModel.setRowCount(0);
        int rows = Math.max(debits.size(), credits.size());
        for (int i = 0; i < rows; i++) {
            Object dAcc = i < debits.size() ? debits.get(i).account : "";
            Object dAmt = i < debits.size() ? debits.get(i).amount : null;
            Object cAcc = i < credits.size() ? credits.get(i).account : "";
            Object cAmt = i < credits.size() ? credits.get(i).amount : null;
            ledgerModel.addRow(new Object[]{dAcc, dAmt, cAcc, cAmt});
        }

        // Totals row
        ledgerModel.addRow(new Object[]{"TOTAL", debitSum, "TOTAL", creditSum});

        debitTotalLabel.setText(UIUtil.money(debitSum));
        creditTotalLabel.setText(UIUtil.money(creditSum));
        netIncomeLabel.setText(UIUtil.money(netIncome));

        String result = netIncome >= 0 ? "PROFIT" : "LOSS";
        noteLabel.setText("Period: " + start + " to " + end +
                " | Sales: " + UIUtil.money(salesTotal) +
                " | Regular staff counted: " + regularCount +
                " | Temp fishermen counted: " + tempCount +
                " | Result: " + result);
    }

    private double sum(List<Line> lines) {
        double s = 0.0;
        for (Line l : lines) s += l.amount;
        return s;
    }

    private double toDouble(Object o) {
        if (o == null) return 0.0;
        if (o instanceof Number) return ((Number) o).doubleValue();
        try {
            String s = String.valueOf(o).trim().replace(",", "").replace("\u09F3", "");
            if (s.isEmpty()) return 0.0;
            return Double.parseDouble(s);
        } catch (Exception ignored) {
            return 0.0;
        }
    }

    private String fmt2(double v) {
        return String.format("%.2f", v);
    }
}
