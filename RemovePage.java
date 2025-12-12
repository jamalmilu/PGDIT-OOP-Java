package org.example;

import javax.swing.*;
import java.awt.*;

public class RemovePage extends JFrame {

    HRDatabase db = new HRDatabase();
    JTextField idField;

    public RemovePage() {
        setTitle("Remove & Show Applicants");
        setSize(400, 300);
        setLocationRelativeTo(null);

        idField = new JTextField();
        JButton removeBtn = new JButton("Remove by ID");
        JButton showBtn = new JButton("Show All");
        JButton backBtn = new JButton("Back");

        removeBtn.addActionListener(e -> remove());
        showBtn.addActionListener(e -> new ShowAllPage().setVisible(true));
        backBtn.addActionListener(e -> {
            new LandingPage().setVisible(true);
            dispose();
        });

        setLayout(new GridLayout(4, 1, 5, 5));
        add(new JLabel("Enter ID to remove:"));
        add(idField);
        add(removeBtn);
        add(showBtn);
        add(backBtn);
    }

    private void remove() {
        try {
            int id = Integer.parseInt(idField.getText());
            db.deleteApplicant(id);
            JOptionPane.showMessageDialog(this, "Applicant Deleted!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }
}
