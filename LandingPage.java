package org.example;

import javax.swing.*;
import java.awt.*;

public class LandingPage extends JFrame {

    public LandingPage() {
        setTitle("HR Database - Main Menu");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton addBtn = new JButton("Add a Person");
        JButton searchBtn = new JButton("Search");
        JButton removeShowBtn = new JButton("Remove & Show");

        addBtn.addActionListener(e -> new AddPersonPage().setVisible(true));
        searchBtn.addActionListener(e -> new SearchPage().setVisible(true));
        removeShowBtn.addActionListener(e -> new RemovePage().setVisible(true));

        setLayout(new GridLayout(3, 1, 10, 10));
        add(addBtn);
        add(searchBtn);
        add(removeShowBtn);
    }
}
