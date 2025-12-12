package org.example;

import javax.swing.*;
import java.awt.*;

public class SearchPage extends JFrame {

    HRDatabase db = new HRDatabase();

    JTextField idField;
    JTextArea resultArea;

    public SearchPage() {
        setTitle("Search Applicant");
        setSize(400, 350);
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new GridLayout(2, 2, 5, 5));

        top.add(new JLabel("Enter ID:"));
        idField = new JTextField();
        top.add(idField);

        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> search());
        top.add(searchBtn);

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            new LandingPage().setVisible(true);
            dispose();
        });
        top.add(backBtn);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        setLayout(new BorderLayout());
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
    }

    private void search() {
        try {
            int id = Integer.parseInt(idField.getText());

            var list = db.getApplicantsList();
            for (var row : list) {
                if (Integer.parseInt(row[0]) == id) {
                    resultArea.setText(
                            "ID: " + row[0] +
                                    "\nName: " + row[1] +
                                    "\nAge: " + row[2] +
                                    "\nPosition: " + row[3]
                    );
                    return;
                }
            }
            resultArea.setText("No record found!");
        } catch (Exception e) {
            resultArea.setText("Invalid ID!");
        }
    }
}
