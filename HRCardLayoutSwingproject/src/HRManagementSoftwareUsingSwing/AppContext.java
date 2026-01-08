package HRManagementSoftwareUsingSwing;

public class AppContext {
    private final HRDatabase db;
    private MainFrame frame;

    public AppContext(HRDatabase db) {
        this.db = db;
    }

    public HRDatabase db() {
        return db;
    }

    public void setFrame(MainFrame frame) {
        this.frame = frame;
    }

    public void go(String screenId) {
        if (frame != null) frame.showScreen(screenId);
    }

    public void logout() {
        if (frame != null) frame.showScreen(MainFrame.SCREEN_LOGIN);
    }
}
