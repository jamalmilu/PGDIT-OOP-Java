package Jattrapur_Fish_Farm_HRM;

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
        JButton logoutBtn = new JButton("Logout");

        addBtn.addActionListener(e -> {
            new AddPersonPage().setVisible(true);
            dispose();
        });
        searchBtn.addActionListener(e -> {
            new SearchPage().setVisible(true);
            dispose();
        });
        removeShowBtn.addActionListener(e -> {
            new RemovePage().setVisible(true);
            dispose();
        });
        logoutBtn.addActionListener(e -> {
            new LoginPage().setVisible(true);
            dispose();
        });

        setLayout(new GridLayout(4, 1, 10, 10));
        add(addBtn);
        add(searchBtn);
        add(removeShowBtn);
        add(logoutBtn);
    }
}
