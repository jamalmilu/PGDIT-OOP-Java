package HRDatabase_UI_Swing_Login;

import javax.swing.*;
import java.awt.*;

public class AddPersonPage extends JFrame {

    private final HRDatabase db = AppContext.DB;

    JTextField idField, nameField, ageField, positionField;

    public AddPersonPage() {
        setTitle("Add Person");
        setSize(400, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));

        form.add(new JLabel("ID:"));
        idField = new JTextField();
        form.add(idField);

        form.add(new JLabel("Name:"));
        nameField = new JTextField();
        form.add(nameField);

        form.add(new JLabel("Age:"));
        ageField = new JTextField();
        form.add(ageField);

        form.add(new JLabel("Position:"));
        positionField = new JTextField();
        form.add(positionField);

        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> addPerson());
        form.add(addBtn);

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            new LandingPage().setVisible(true);
            dispose();
        });
        form.add(backBtn);

        add(form);
    }

    private void addPerson() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText() == null ? "" : nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String pos = positionField.getText() == null ? "" : positionField.getText().trim();

            if (name.isEmpty() || pos.isEmpty()) {
                UIUtil.showError(this, "Name and Position cannot be empty!");
                return;
            }

            db.insertApplicant(id, name, age, pos);
            UIUtil.showInfo(this, "Successfully Added!");
            idField.setText("");
            nameField.setText("");
            ageField.setText("");
            positionField.setText("");

        } catch (Exception ex) {
            UIUtil.showError(this, "Invalid input!");
        }
    }
}
