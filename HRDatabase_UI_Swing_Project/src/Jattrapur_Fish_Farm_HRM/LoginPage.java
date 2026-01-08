package Jattrapur_Fish_Farm_HRM;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {
    private final HRDatabase db = AppContext.DB;

    private JTextField phoneField;
    private JPasswordField passField;

    public LoginPage() {
        setTitle("HR System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Safety: If someone runs LoginPage directly (not from MainUI), ensure tables exist.
        db.createTable();

        // Outer container (center card)
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        // Card panel (fresh look)
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        card.setPreferredSize(new Dimension(360, 270));

        // Header
        JLabel title = new JLabel("HR System");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel subtitle = new JLabel("Sign in to continue");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(new Color(90, 90, 90));

        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(16));

        // Fields
        phoneField = new JTextField();
        passField = new JPasswordField();
        char defaultEcho = passField.getEchoChar();

        card.add(labeledField("Phone", phoneField));
        card.add(Box.createVerticalStrut(10));
        card.add(labeledField("Password", passField));

        // Show password
        JCheckBox showPass = new JCheckBox("Show password");
        showPass.setOpaque(false);
        showPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        showPass.addActionListener(e -> {
            if (showPass.isSelected()) {
                passField.setEchoChar((char) 0);
            } else {
                passField.setEchoChar(defaultEcho);
            }
        });

        card.add(Box.createVerticalStrut(8));
        card.add(showPass);
        card.add(Box.createVerticalStrut(14));

        // Login button
        JButton loginBtn = new JButton("Login");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setPreferredSize(new Dimension(0, 36));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        loginBtn.addActionListener(e -> handleLogin());

        // Enter press will login
        getRootPane().setDefaultButton(loginBtn);

        card.add(loginBtn);
        card.add(Box.createVerticalStrut(10));

        // Test hint label
        JLabel testLabel = new JLabel("Test Login: 12345 & password", SwingConstants.CENTER);
        testLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        testLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        testLabel.setForeground(new Color(120, 120, 120));

        card.add(testLabel);

        outer.add(card);
        setContentPane(outer);

        pack();
        setLocationRelativeTo(null);

        // UX: focus cursor on phone
        SwingUtilities.invokeLater(() -> phoneField.requestFocusInWindow());
    }

    private JPanel labeledField(String label, JComponent field) {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setOpaque(false);
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(new Color(60, 60, 60));

        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(0, 30));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        wrap.add(lbl);
        wrap.add(Box.createVerticalStrut(4));
        wrap.add(field);

        return wrap;
    }

    private void handleLogin() {
        String phone = phoneField.getText() == null ? "" : phoneField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        if (phone.isEmpty() || password.isEmpty()) {
            UIUtil.showError(this, "Phone and Password cannot be empty!");
            return;
        }

        if (db.authenticate(phone, password)) {
            new LandingPage().setVisible(true);
            dispose();
        } else {
            UIUtil.showError(this, "Invalid credentials!\nTest user: phone=12345, pass=password");
        }
    }
}
