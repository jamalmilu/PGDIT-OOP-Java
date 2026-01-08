package HRManagementSoftwareUsingSwing;

import javax.swing.SwingUtilities;

public class MainUI {
    public static void main(String[] args) {
        // Always start Swing apps on the EDT
        SwingUtilities.invokeLater(() -> {
            UIUtil.setSystemLookAndFeel();

            HRDatabase db = new HRDatabase();
            db.createTable();

            AppContext ctx = new AppContext(db);
            MainFrame frame = new MainFrame(ctx);
            ctx.setFrame(frame);

            frame.setVisible(true);
        });
    }
}
