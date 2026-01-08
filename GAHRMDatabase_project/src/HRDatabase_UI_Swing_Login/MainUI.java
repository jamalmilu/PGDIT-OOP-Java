package HRDatabase_UI_Swing_Login;

public class MainUI {
    public static void main(String[] args) {
        UIUtil.installSystemLookAndFeel();

        // Use the shared DB instance across the entire app.
        // This creates BOTH applicants + user tables (and inserts a default user if empty).
        AppContext.DB.createTable();

        javax.swing.SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}