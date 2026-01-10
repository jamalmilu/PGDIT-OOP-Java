import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {
    private final DatabaseManager db = DatabaseManager.getInstance();

    public MainDashboard(String userPhone, String userName, String userRole) {
        setTitle("Fish Farm Dashboard - " + userName + " (" + userRole + ")");
        setSize(1200, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(buildTopBar(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Fish Stock", new FishStockPanel(db));
        tabs.addTab("Sales", new SalesPanel(db));
        tabs.addTab("Expenses", new ExpensePanel(db));
        tabs.addTab("Staff", new StaffPanel(db));
        tabs.addTab("Profit & Loss", new ProfitLossPanel(db));
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new GridLayout(1, 4, 10, 10));
        top.setBorder(BorderFactory.createTitledBorder("Summary"));

        double purchasedKg = getDouble("SELECT COALESCE(SUM(quantity_kg),0) FROM FishStock");
        double soldKg = getDouble("SELECT COALESCE(SUM(quantity_kg),0) FROM Sales");
        double currentStockKg = purchasedKg - soldKg;

        double fishStockCost = getDouble("SELECT COALESCE(SUM(total_cost),0) FROM FishStock");
        double totalExpenses = getDouble("SELECT COALESCE(SUM(total_cost),0) FROM Expenses");
        double salesRevenue = getDouble("SELECT COALESCE(SUM(total_amount),0) FROM Sales");

        JLabel a = new JLabel("Current Fish Stock (kg): " + UiUtil.num2(currentStockKg), JLabel.CENTER);
        JLabel b = new JLabel("Fish Stock Cost: " + UiUtil.money(fishStockCost), JLabel.CENTER);
        JLabel c = new JLabel("Total Expenses: " + UiUtil.money(totalExpenses), JLabel.CENTER);
        JLabel d = new JLabel("Sales Revenue: " + UiUtil.money(salesRevenue), JLabel.CENTER);

        a.setFont(a.getFont().deriveFont(Font.BOLD, 16f));
        b.setFont(b.getFont().deriveFont(Font.BOLD, 16f));
        c.setFont(c.getFont().deriveFont(Font.BOLD, 16f));
        d.setFont(d.getFont().deriveFont(Font.BOLD, 16f));

        top.add(a);
        top.add(b);
        top.add(c);
        top.add(d);

        return top;
    }

    private double getDouble(String sql) {
        Object[] row = db.querySingleRow(sql);
        if (row == null || row.length == 0 || row[0] == null) return 0.0;
        if (row[0] instanceof Number n) return n.doubleValue();
        try { return Double.parseDouble(row[0].toString()); } catch (Exception e) { return 0.0; }
    }

    // Formatting handled by UiUtil
}
