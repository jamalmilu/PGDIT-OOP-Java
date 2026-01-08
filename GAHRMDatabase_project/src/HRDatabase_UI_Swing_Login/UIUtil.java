package HRDatabase_UI_Swing_Login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Small UI helper utilities to keep the Swing pages consistent and cleaner.
 */
public final class UIUtil {
    private UIUtil() {
        // Utility class
    }

    public static void installSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // If LAF fails, Swing will fall back to default.
        }
    }

    public static JPanel paddedPanel(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        return p;
    }

    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirm(Component parent, String message) {
        int choice = JOptionPane.showConfirmDialog(parent, message, "Confirm", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }
}
