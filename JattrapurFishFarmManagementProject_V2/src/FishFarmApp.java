import javax.swing.*;

public class FishFarmApp {
    public static void main(String[] args) {
        // Initialize DB first (creates tables)
        DatabaseManager.getInstance();

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            LoginFrame lf = new LoginFrame();
            lf.setVisible(true);
        });
    }
}
