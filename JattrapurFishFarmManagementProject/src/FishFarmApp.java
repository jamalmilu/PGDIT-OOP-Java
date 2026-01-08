import javax.swing.*;

public class FishFarmApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // Set better font for Bengali/English mixed text
                UIManager.put("Label.font", new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
                UIManager.put("Button.font", new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
                UIManager.put("TextField.font", new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Initialize database connection
            DatabaseManager.getInstance();

            // Start with login frame
            new LoginFrame();
        });
    }
}