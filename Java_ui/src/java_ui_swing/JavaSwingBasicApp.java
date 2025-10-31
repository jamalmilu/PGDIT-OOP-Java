package java_ui_swing;

import javax.swing.*;

public class JavaSwingBasicApp {
    public void basicUI(){
    //The method to build and display the basic UI.

        //Creates a window titled "Jamal's basic Swing App".
        JFrame mainFrame = new JFrame("Jamal's basic Swing App");
        mainFrame.setSize(400,400);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel showHello = new JLabel("Welcome to Goriber Asroy!!!");

        showHello.setHorizontalAlignment(SwingConstants.CENTER);

        mainFrame.getContentPane().add(showHello);
        mainFrame.setVisible(true);

    }
}
