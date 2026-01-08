package JFrame_Login_App;

import javax.swing.*;
import java.awt.*;

public class MyLoginApp {
    public static void main(String[] args) {
        // Object Creation
        JFrame frame = new JFrame();

        // Set title of the window
        frame.setTitle("User Login");

        // Set frame window size
        frame.setSize(400,300);

        // Set Layout management
        frame.setLayout(null);

        // Add component to
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150,100,100,40);
        frame.add(loginButton);

        // Set close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Stop resizing the frame window
        frame.setResizable(false);

        // Align to the center
        frame.setLocationRelativeTo(null);

        // change background color of the frame
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);

        // Set frame visible
        frame.setVisible(true);

    }
}
