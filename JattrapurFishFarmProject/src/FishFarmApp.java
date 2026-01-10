import javax.swing.*;

public class FishFarmApp {
    public static void main(String[] args) {
        // Initialize DB once
        DatabaseManager.getInstance();

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            // Global UI readability
            UiUtil.setGlobalFontSize(16f);
            new LoginFrame().setVisible(true);
        });
    }
}
