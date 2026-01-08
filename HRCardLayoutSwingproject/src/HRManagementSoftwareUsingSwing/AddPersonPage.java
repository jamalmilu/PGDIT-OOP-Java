package HRManagementSoftwareUsingSwing;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AddPersonPage extends JPanel {

    private final AppContext ctx;

    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField ageField = new JTextField();
    private final JTextField positionField = new JTextField();

    public AddPersonPage(AppContext ctx) {
        this.ctx = ctx;

        setLayout(new BorderLayout());
        JPanel form = UIUtil.paddedPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Add Applicant"));

        GridBagConstraints c;

        c = UIUtil.gbc(0, 0);
        form.add(new JLabel("ID:"), c);
        c = UIUtil.gbc(1, 0); c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        form.add(idField, c);

        c = UIUtil.gbc(0, 1);
        form.add(new JLabel("Name:"), c);
        c = UIUtil.gbc(1, 1); c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        form.add(nameField, c);

        c = UIUtil.gbc(0, 2);
        form.add(new JLabel("Age:"), c);
        c = UIUtil.gbc(1, 2); c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        form.add(ageField, c);

        c = UIUtil.gbc(0, 3);
        form.add(new JLabel("Position:"), c);
        c = UIUtil.gbc(1, 3); c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        form.add(positionField, c);

        JButton addBtn = new JButton("Add");
        JButton clearBtn = new JButton("Clear");
        JButton backBtn = new JButton("Back to Menu");

        addBtn.addActionListener(e -> addPerson());
        clearBtn.addActionListener(e -> clear());
        backBtn.addActionListener(e -> ctx.go(MainFrame.SCREEN_MENU));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(backBtn);
        actions.add(clearBtn);
        actions.add(addBtn);

        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(addBtn);
    }

    private void clear() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        positionField.setText("");
        idField.requestFocusInWindow();
    }

    private void addPerson() {
        String idText = UIUtil.trimmed(idField);
        String name = UIUtil.trimmed(nameField);
        String ageText = UIUtil.trimmed(ageField);
        String post = UIUtil.trimmed(positionField);

        if (idText.isEmpty() || name.isEmpty() || ageText.isEmpty() || post.isEmpty()) {
            UIUtil.error(this, "All fields are required.");
            return;
        }

        int id, age;
        try {
            id = Integer.parseInt(idText);
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException ex) {
            UIUtil.error(this, "ID and Age must be numbers.");
            return;
        }

        if (id <= 0) {
            UIUtil.error(this, "ID must be a positive number.");
            return;
        }
        if (age < 0 || age > 120) {
            UIUtil.error(this, "Age must be between 0 and 120.");
            return;
        }

        try {
            boolean ok = ctx.db().insertApplicant(id, name, age, post);
            if (ok) {
                UIUtil.info(this, "Applicant added successfully.");
                clear();
            } else {
                UIUtil.error(this, "Could not add applicant.");
            }
        } catch (SQLException ex) {
            // Most common: duplicate PRIMARY KEY (same ID)
            UIUtil.error(this, "Failed to add. This ID may already exist.");
        }
    }
}
