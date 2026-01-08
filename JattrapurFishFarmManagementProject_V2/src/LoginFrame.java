import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final DatabaseManager db = DatabaseManager.getInstance();

    private final JTextField phoneField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18);

    public LoginFrame() {
        setTitle("Fish Farm - Login");
        setSize(420, 230);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0; p.add(new JLabel("Phone"), g);
        g.gridx = 1; p.add(phoneField, g);

        g.gridx = 0; g.gridy = 1; p.add(new JLabel("Password"), g);
        g.gridx = 1; p.add(passwordField, g);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.add(registerBtn);
        btns.add(loginBtn);

        g.gridx = 0; g.gridy = 2; g.gridwidth = 2;
        p.add(btns, g);

        setContentPane(p);

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> new RegisterDialog(this, db).setVisible(true));
    }

    private void login() {
        String phone = phoneField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (phone.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone and password are required.");
            return;
        }

        String hash = PasswordUtil.sha256(pass);

        Object[] row = db.querySingleRow(
                "SELECT phone, name, role FROM Users WHERE phone=? AND password_hash=?",
                phone, hash
        );

        if (row == null) {
            JOptionPane.showMessageDialog(this, "Invalid phone or password.");
            return;
        }

        dispose();
        MainDashboard dash = new MainDashboard((String) row[0], (String) row[1], (String) row[2]);
        dash.setVisible(true);
    }

    // Simple registration dialog (keeps your project small and clean).
    private static class RegisterDialog extends JDialog {
        private final DatabaseManager db;

        private final JTextField nameField = new JTextField(18);
        private final JTextField phoneField = new JTextField(18);
        private final JPasswordField pass1Field = new JPasswordField(18);
        private final JPasswordField pass2Field = new JPasswordField(18);

        RegisterDialog(Frame owner, DatabaseManager db) {
            super(owner, "Register", true);
            this.db = db;

            setSize(460, 300);
            setLocationRelativeTo(owner);

            JPanel p = new JPanel(new GridBagLayout());
            GridBagConstraints g = new GridBagConstraints();
            g.insets = new Insets(8, 8, 8, 8);
            g.fill = GridBagConstraints.HORIZONTAL;

            g.gridx=0; g.gridy=0; p.add(new JLabel("Full Name"), g);
            g.gridx=1; p.add(nameField, g);

            g.gridx=0; g.gridy=1; p.add(new JLabel("Phone"), g);
            g.gridx=1; p.add(phoneField, g);

            g.gridx=0; g.gridy=2; p.add(new JLabel("Password"), g);
            g.gridx=1; p.add(pass1Field, g);

            g.gridx=0; g.gridy=3; p.add(new JLabel("Confirm Password"), g);
            g.gridx=1; p.add(pass2Field, g);

            JButton createBtn = new JButton("Create Account");
            g.gridx=1; g.gridy=4;
            p.add(createBtn, g);

            setContentPane(p);

            createBtn.addActionListener(e -> register());
        }

        private void register() {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String p1 = new String(pass1Field.getPassword());
            String p2 = new String(pass2Field.getPassword());

            if (name.isEmpty() || phone.isEmpty() || p1.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }
            if (!p1.equals(p2)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            String hash = PasswordUtil.sha256(p1);

            int rows = db.executeUpdate(
                    "INSERT INTO Users(name, phone, password_hash, role) VALUES(?,?,?,?)",
                    name, phone, hash, "USER"
            );

            if (rows == 1) {
                JOptionPane.showMessageDialog(this, "Registration successful. Please login.");
                dispose();
            }
        }
    }
}
