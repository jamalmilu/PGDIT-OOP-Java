import javax.swing.*;

public class FishFarmApp {
    public static void main(String[] args) {
        // Initialize DB once
        DatabaseManager.getInstance();

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            // Make UI readable
            UIUtil.setGlobalFontSize(16);

            new LoginFrame().setVisible(true);
        });
    }
}
