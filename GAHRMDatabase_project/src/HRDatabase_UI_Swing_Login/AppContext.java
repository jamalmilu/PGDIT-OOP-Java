package HRDatabase_UI_Swing_Login;

/**
 * A tiny shared context to avoid creating multiple HRDatabase instances
 * across different pages.
 */
public final class AppContext {
    private AppContext() {
        // Utility class
    }

    public static final HRDatabase DB = new HRDatabase();
}
