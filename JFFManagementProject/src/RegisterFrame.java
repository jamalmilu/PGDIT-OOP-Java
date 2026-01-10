import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private final DatabaseManager db = DatabaseManager.getInstance();

    private final JTextField nameField = new JTextField(18);
    private final JTextField phoneField = new JTextField(18);
    private final JPasswordField passField = new JPasswordField(18);
    private final JPasswordField pass2Field = new JPasswordField(18);

    public RegisterFrame() {
        setTitle("Fish Farm - Register");
        setSize(460, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx=0; g.gridy=0; p.add(new JLabel("Full Name"), g);
        g.gridx=1; p.add(nameField, g);

        g.gridx=0; g.gridy=1; p.add(new JLabel("Phone"), g);
        g.gridx=1; p.add(phoneField, g);

        g.gridx=0; g.gridy=2; p.add(new JLabel("Password"), g);
        g.gridx=1; p.add(passField, g);

        g.gridx=0; g.gridy=3; p.add(new JLabel("Confirm Password"), g);
        g.gridx=1; p.add(pass2Field, g);

        JButton createBtn = new JButton("Create Account");
        g.gridx=1; g.gridy=4;
        p.add(createBtn, g);

        createBtn.addActionListener(e -> register());

        setContentPane(p);
    }

    private void register() {
        String fullName = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String pass = new String(passField.getPassword());
        String pass2 = new String(pass2Field.getPassword());

        if (fullName.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }
        if (!pass.equals(pass2)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        String hash = PasswordUtil.sha256(pass);
        int rows = db.executeUpdate(
                "INSERT INTO Users(full_name, phone, password_hash, role) VALUES(?,?,?,?)",
                fullName, phone, hash, "USER"
        );

        if (rows == 1) {
            JOptionPane.showMessageDialog(this, "Registration successful. Please login.");
            dispose();
        }
    }
}
