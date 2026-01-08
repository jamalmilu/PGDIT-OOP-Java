package HRManagementSoftwareUsingSwing;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final String SCREEN_LOGIN = "login";
    public static final String SCREEN_MENU = "menu";
    public static final String SCREEN_ADD = "add";
    public static final String SCREEN_SEARCH = "search";
    public static final String SCREEN_SHOW_ALL = "show_all";
    public static final String SCREEN_REMOVE = "remove";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    private final AppContext ctx;

    public MainFrame(AppContext ctx) {
        this.ctx = ctx;

        setTitle("HR Database");
        setSize(720, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add screens ONCE (no more new JFrame on every click)
        addScreen(SCREEN_LOGIN, new LoginPage(ctx));
        addScreen(SCREEN_MENU, new LandingPage(ctx));
        addScreen(SCREEN_ADD, new AddPersonPage(ctx));
        addScreen(SCREEN_SEARCH, new SearchPage(ctx));
        addScreen(SCREEN_SHOW_ALL, new ShowAllPage(ctx));
        addScreen(SCREEN_REMOVE, new RemovePage(ctx));

        setContentPane(cards);
        showScreen(SCREEN_LOGIN);
    }

    private void addScreen(String id, JComponent screen) {
        cards.add(screen, id);
    }

    public void showScreen(String id) {
        cardLayout.show(cards, id);

        // If the screen needs to refresh data when shown, call it here.
        for (Component comp : cards.getComponents()) {
            if (comp.isVisible() && comp instanceof Refreshable r) {
                r.onShow();
            }
        }
    }
}
