package org.example;

public class MainUI {
    public static void main(String[] args) {
        HRDatabase hrDatabase = new HRDatabase();
        hrDatabase.createTable();
        javax.swing.SwingUtilities.invokeLater(() ->
                new LandingPage().setVisible(true)
        );
    }
}
