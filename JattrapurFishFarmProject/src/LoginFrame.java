import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final DatabaseManager db = DatabaseManager.getInstance();

    private final JTextField phoneField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18);

    public LoginFrame() {
        setTitle("Fish Farm - Login");
        setSize(440, 240);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx=0; g.gridy=0; p.add(new JLabel("Phone"), g);
        g.gridx=1; p.add(phoneField, g);

        g.gridx=0; g.gridy=1; p.add(new JLabel("Password"), g);
        g.gridx=1; p.add(passwordField, g);

        JButton regBtn = new JButton("Register");
        JButton loginBtn = new JButton("Login");

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.add(regBtn);
        btns.add(loginBtn);

        g.gridx=0; g.gridy=2; g.gridwidth=2;
        p.add(btns, g);

        regBtn.addActionListener(e -> new RegisterFrame().setVisible(true));
        loginBtn.addActionListener(e -> login());

        setContentPane(p);
    }

    private void login() {
        String phone = phoneField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (phone.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone and password are required.");
            return;
        }

        String hash = PasswordUtil.sha256(pass);

        Object[] user = db.querySingleRow(
                "SELECT id, phone, full_name, role FROM Users WHERE phone=? AND password_hash=?",
                phone, hash
        );

        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid phone or password.");
            return;
        }

        dispose();
        MainDashboard dash = new MainDashboard((String) user[1], (String) user[2], (String) user[3]);
        dash.setVisible(true);
    }
}
