package HRManagementSoftwareUsingSwing;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JPanel {

    private final AppContext ctx;
    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final char defaultEchoChar;

    public LoginPage(AppContext ctx) {
        this.ctx = ctx;
        this.defaultEchoChar = passwordField.getEchoChar();

        setLayout(new GridBagLayout());
        JPanel card = UIUtil.paddedPanel(new GridBagLayout());
        card.setBorder(BorderFactory.createTitledBorder("Login"));

        GridBagConstraints c;

        c = UIUtil.gbc(0, 0);
        card.add(new JLabel("Username:"), c);

        c = UIUtil.gbc(1, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        usernameField.setColumns(18);
        card.add(usernameField, c);

        c = UIUtil.gbc(0, 1);
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        card.add(new JLabel("Password:"), c);

        c = UIUtil.gbc(1, 1);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        passwordField.setColumns(18);
        card.add(passwordField, c);

        JCheckBox show = new JCheckBox("Show password");
        show.addActionListener(e ->
                passwordField.setEchoChar(show.isSelected() ? (char) 0 : defaultEchoChar)
        );

        c = UIUtil.gbc(1, 2);
        c.anchor = GridBagConstraints.WEST;
        card.add(show, c);

        JButton loginBtn = new JButton("Login");
        JButton clearBtn = new JButton("Clear");

        loginBtn.addActionListener(e -> login());
        clearBtn.addActionListener(e -> clear());
        passwordField.addActionListener(e -> login());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(clearBtn);
        actions.add(loginBtn);

        c = UIUtil.gbc(0, 3);
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        card.add(actions, c);

        add(card);
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
        getRootPane().setDefaultButton(loginBtn);
    }

    private void clear() {
        usernameField.setText("");
        passwordField.setText("");
        usernameField.requestFocusInWindow();
    }

    private void login() {
        String u = UIUtil.trimmed(usernameField);
        String p = new String(passwordField.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            UIUtil.error(this, "Username and password are required.");
            return;
        }

        if (authenticate(u, p)) {
            clear();
            ctx.go(MainFrame.SCREEN_MENU);
        } else {
            UIUtil.error(this, "Invalid username or password.");
            passwordField.setText("");
            passwordField.requestFocusInWindow();
        }
    }

    // Demo login (change if your assignment requires DB users)
    private boolean authenticate(String username, String password) {
        return "admin".equals(username) && "admin".equals(password);
    }
}
