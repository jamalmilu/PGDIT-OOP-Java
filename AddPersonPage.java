package org.example;

import javax.swing.*;
import java.awt.*;

public class AddPersonPage extends JFrame {

    HRDatabase db = new HRDatabase();

    JTextField idField, nameField, ageField, positionField;

    public AddPersonPage() {
        setTitle("Add Person");
        setSize(400, 330);
        setLocationRelativeTo(null);

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
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String pos = positionField.getText();

            db.insertApplicant(id, name, age, pos);
            JOptionPane.showMessageDialog(this, "Successfully Added!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }
}
