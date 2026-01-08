import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private DatabaseManager db = DatabaseManager.getInstance();
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JButton loginBtn, registerBtn;

    public LoginFrame() {
        setTitle("Fish Farm - Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Phone
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Phone Number:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        phoneField = new JTextField(15);
        panel.add(phoneField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());

        loginBtn = new JButton("Login");
        registerBtn = new JButton("Register");
        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> register());

        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        panel.add(buttonPanel, gbc);

        add(panel);
        setVisible(true);
    }

    private void login() {
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter phone and password!");
            return;
        }

        Object[] user = db.querySingleRow("SELECT id, phone, full_name, role FROM Users WHERE phone=? AND password=?", phone, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + user[2]);
            dispose();
            new MainDashboard((String) user[2]); // full name
        } else {
            JOptionPane.showMessageDialog(this, "Invalid phone or password!");
        }
    }

    private void register() {
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter phone and password!");
            return;
        }

        // Check if user already exists
        if (db.userExists(phone)) {
            JOptionPane.showMessageDialog(this, "This phone number is already registered!");
            return;
        }

        // Register new user
        String name = JOptionPane.showInputDialog(this, "Enter your full name:");
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required!");
            return;
        }

        String email = JOptionPane.showInputDialog(this, "Enter email (optional):");

        String sql = "INSERT INTO Users (phone, password, full_name, email, role) VALUES (?, ?, ?, ?, 'user')";

        try (Connection conn = DriverManager.getConnection(DatabaseManager.DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phone);
            stmt.setString(2, password);
            stmt.setString(3, name.trim());
            stmt.setString(4, email != null ? email.trim() : "");
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration Successful! Please login.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Registration failed: " + e.getMessage());
        }
    }
}