import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Profit & Loss in a classic "T-format" (Debit vs Credit) with line-by-line breakdown.
 */
public class ProfitLossPanel extends JPanel {
    private final DatabaseManager db;

    private final JTextField startDateField = new JTextField();
    private final JTextField endDateField = new JTextField();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Debit (Account)", "Debit (\u09F3)", "Credit (Account)", "Credit (\u09F3)"}, 0
    ) {
        @Override public boolean isCellEditable(int r, int c) { return false; }

        @Override public Class<?> getColumnClass(int columnIndex) {
            return (columnIndex == 1 || columnIndex == 3) ? Double.class : String.class;
        }
    };

    private final JTable table = new JTable(model);

    private final JLabel summaryLabel = new JLabel(" ");

    public ProfitLossPanel(DatabaseManager db) {
        this.db = db;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Profit & Loss (T-Format)"));

        LocalDate today = LocalDate.now();
        startDateField.setText(today.withDayOfMonth(1).toString());
        endDateField.setText(today.toString());

        UiUtil.styleTable(table);
        UiUtil.applyCurrencyRenderer(table, 1, 3);

        add(buildControls(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        summaryLabel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        summaryLabel.setFont(summaryLabel.getFont().deriveFont(Font.BOLD, 16f));
        add(summaryLabel, BorderLayout.SOUTH);

        calculate();
    }

    private JPanel buildControls() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        int r = 0;
        g.gridx = 0; g.gridy = r; p.add(new JLabel("Start Date (YYYY-MM-DD)"), g);
        g.gridx = 1; g.weightx = 1; p.add(startDateField, g);
        g.weightx = 0;

        g.gridx = 2; p.add(new JLabel("End Date (YYYY-MM-DD)"), g);
        g.gridx = 3; g.weightx = 1; p.add(endDateField, g);
        g.weightx = 0;

        r++;
        JButton thisMonth = new JButton("This Month");
        JButton lastMonth = new JButton("Last Month");
        JButton calculateBtn = new JButton("Calculate");

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.add(lastMonth);
        btns.add(thisMonth);
        btns.add(calculateBtn);

        g.gridx = 0; g.gridy = r; g.gridwidth = 4;
        p.add(btns, g);

        thisMonth.addActionListener(e -> setThisMonth());
        lastMonth.addActionListener(e -> setLastMonth());
        calculateBtn.addActionListener(e -> calculate());

        return p;
    }

    private void setThisMonth() {
        LocalDate today = LocalDate.now();
        startDateField.setText(today.withDayOfMonth(1).toString());
        endDateField.setText(today.toString());
        calculate();
    }

    private void setLastMonth() {
        LocalDate today = LocalDate.now();
        YearMonth last = YearMonth.from(today).minusMonths(1);
        startDateField.setText(last.atDay(1).toString());
        endDateField.setText(last.atEndOfMonth().toString());
        calculate();
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

        DatabaseManager.ProfitLossLedger ledger;
        try {
            ledger = db.getProfitLossLedger(start, end);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Calculation error: " + ex.getMessage());
            return;
        }

        renderTFormat(ledger);
    }

    private void renderTFormat(DatabaseManager.ProfitLossLedger ledger) {
        model.setRowCount(0);

        List<DatabaseManager.LedgerEntry> debits = ledger.debits;
        List<DatabaseManager.LedgerEntry> credits = ledger.credits;

        int rows = Math.max(debits.size(), credits.size());
        for (int i = 0; i < rows; i++) {
            String dAcc = "";
            Double dAmt = null;
            String cAcc = "";
            Double cAmt = null;

            if (i < debits.size()) {
                dAcc = debits.get(i).account;
                dAmt = debits.get(i).amount;
            }
            if (i < credits.size()) {
                cAcc = credits.get(i).account;
                cAmt = credits.get(i).amount;
            }

            model.addRow(new Object[]{dAcc, dAmt, cAcc, cAmt});
        }

        // Total row (balanced)
        model.addRow(new Object[]{"TOTAL", ledger.totalDebits, "TOTAL", ledger.totalCredits});

        String result = ledger.netIncome >= 0 ? "PROFIT" : "LOSS";
        String net = UiUtil.money(Math.abs(ledger.netIncome));

        summaryLabel.setText(
                "Period: " + ledger.start + " to " + ledger.end +
                        " | Sales: " + UiUtil.money(ledger.salesRevenue) +
                        " | Regular Staff: " + ledger.regularStaffCount +
                        " | Temp Fishermen: " + ledger.tempFishermenCount +
                        " | Result: " + result + " " + net
        );

        // Make the TOTAL row bold-ish by increasing its height slightly
        table.scrollRectToVisible(table.getCellRect(Math.max(0, model.getRowCount() - 1), 0, true));
    }
}
