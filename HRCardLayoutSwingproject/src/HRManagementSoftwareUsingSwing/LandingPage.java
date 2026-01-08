package HRManagementSoftwareUsingSwing;

import javax.swing.*;
import java.awt.*;

public class LandingPage extends JPanel {

    private final AppContext ctx;

    public LandingPage(AppContext ctx) {
        this.ctx = ctx;

        setLayout(new BorderLayout());
        JPanel content = UIUtil.paddedPanel(new GridBagLayout());
        content.setBorder(BorderFactory.createTitledBorder("Main Menu"));

        JButton addBtn = new JButton("Add Applicant");
        JButton searchBtn = new JButton("Search by ID");
        JButton showAllBtn = new JButton("Show All");
        JButton removeBtn = new JButton("Remove by ID");
        JButton logoutBtn = new JButton("Logout");

        addBtn.addActionListener(e -> ctx.go(MainFrame.SCREEN_ADD));
        searchBtn.addActionListener(e -> ctx.go(MainFrame.SCREEN_SEARCH));
        showAllBtn.addActionListener(e -> ctx.go(MainFrame.SCREEN_SHOW_ALL));
        removeBtn.addActionListener(e -> ctx.go(MainFrame.SCREEN_REMOVE));
        logoutBtn.addActionListener(e -> ctx.logout());

        GridBagConstraints c;
        c = UIUtil.gbc(0, 0); c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        content.add(addBtn, c);

        c = UIUtil.gbc(0, 1); c.fill = GridBagConstraints.HORIZONTAL;
        content.add(searchBtn, c);

        c = UIUtil.gbc(0, 2); c.fill = GridBagConstraints.HORIZONTAL;
        content.add(showAllBtn, c);

        c = UIUtil.gbc(0, 3); c.fill = GridBagConstraints.HORIZONTAL;
        content.add(removeBtn, c);

        c = UIUtil.gbc(0, 4); c.fill = GridBagConstraints.HORIZONTAL;
        content.add(logoutBtn, c);

        add(content, BorderLayout.CENTER);
    }
}
