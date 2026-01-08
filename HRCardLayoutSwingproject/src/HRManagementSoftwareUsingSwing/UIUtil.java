package HRManagementSoftwareUsingSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class UIUtil {
    private UIUtil() {}

    public static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    public static JPanel paddedPanel(LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        return p;
    }

    public static GridBagConstraints gbc(int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        return c;
    }

    public static void error(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(Component parent, String msg) {
        return JOptionPane.showConfirmDialog(parent, msg, "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static String trimmed(JTextField field) {
        return field.getText() == null ? "" : field.getText().trim();
    }
}
