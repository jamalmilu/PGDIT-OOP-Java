package HRManagementSoftwareUsingSwing;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RemovePage extends JPanel {

    private final AppContext ctx;
    private final JTextField idField = new JTextField();

    public RemovePage(AppContext ctx) {
        this.ctx = ctx;

        setLayout(new BorderLayout());

        JPanel form = UIUtil.paddedPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Remove Applicant"));

        GridBagConstraints c;

        c = UIUtil.gbc(0, 0);
        form.add(new JLabel("Enter ID to remove:"), c);

        c = UIUtil.gbc(1, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        idField.setColumns(12);
        form.add(idField, c);

        JButton removeBtn = new JButton("Remove");
        JButton showAllBtn = new JButton("Show All");
        JButton backBtn = new JButton("Back to Menu");

        removeBtn.addActionListener(e -> remove());
        showAllBtn.addActionListener(e -> ctx.go(MainFrame.SCREEN_SHOW_ALL));
        backBtn.addActionListener(e -> ctx.go(MainFrame.SCREEN_MENU));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(backBtn);
        actions.add(showAllBtn);
        actions.add(removeBtn);

        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(removeBtn);
    }

    private void remove() {
        String idText = UIUtil.trimmed(idField);
        if (idText.isEmpty()) {
            UIUtil.error(this, "Please enter an ID.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            UIUtil.error(this, "Invalid ID (must be a number).");
            return;
        }

        if (!UIUtil.confirm(this, "Delete applicant with ID " + id + "?")) {
            return;
        }

        try {
            boolean ok = ctx.db().deleteApplicant(id);
            if (ok) {
                UIUtil.info(this, "Applicant deleted.");
                idField.setText("");
                idField.requestFocusInWindow();
            } else {
                UIUtil.error(this, "No applicant found with that ID.");
            }
        } catch (SQLException ex) {
            UIUtil.error(this, "Delete failed: " + ex.getMessage());
        }
    }
}
