import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MainDashboard extends JFrame {
    private DatabaseManager db = DatabaseManager.getInstance();
    private String userName;

    public MainDashboard(String userName) {
        this.userName = userName;
        setTitle("Fish Farm Management - Welcome " + userName);
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Create menu bar
        createMenuBar();

        // Create tabbed pane for different sections
        createTabbedPane();

        // Show dashboard stats
        showDashboardStats();

        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem exitItem = new JMenuItem("Exit");

        logoutItem.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Fish Stock", new FishStockPanel(db));
        tabbedPane.addTab("Expenses", new ExpensePanel(db));
        tabbedPane.addTab("Staff", new StaffPanel(db));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void showDashboardStats() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Dashboard Summary"));
        statsPanel.setBackground(new Color(240, 248, 255));

        try (Connection conn = DriverManager.getConnection(DatabaseManager.DB_URL)) {
            // Current fish stock
            PreparedStatement stmt1 = conn.prepareStatement("SELECT SUM(quantity) as total FROM FishStock WHERE status='Active'");
            ResultSet rs1 = stmt1.executeQuery();
            rs1.next();
            int totalFish = rs1.getInt("total");
            if (rs1.wasNull()) totalFish = 0;

            // Total expenses
            PreparedStatement stmt2 = conn.prepareStatement("SELECT SUM(total_cost) as total FROM Expenses");
            ResultSet rs2 = stmt2.executeQuery();
            rs2.next();
            double totalExpense = rs2.getDouble("total");
            if (rs2.wasNull()) totalExpense = 0;

            // Active staff
            PreparedStatement stmt3 = conn.prepareStatement("SELECT COUNT(*) as count FROM Staff WHERE status='Active'");
            ResultSet rs3 = stmt3.executeQuery();
            rs3.next();
            int activeStaff = rs3.getInt("count");

            // Create stat cards
            statsPanel.add(createStatCard("Fish Stock", totalFish + " pieces", Color.BLUE));
            statsPanel.add(createStatCard("Total Expenses", "৳" + String.format("%.2f", totalExpense), Color.RED));
            statsPanel.add(createStatCard("Active Staff", activeStaff + " persons", Color.GREEN));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        add(statsPanel, BorderLayout.NORTH);
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(color, 2));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(color);

        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }
}