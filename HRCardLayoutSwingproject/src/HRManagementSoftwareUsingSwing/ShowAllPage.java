package HRManagementSoftwareUsingSwing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ShowAllPage extends JPanel implements Refreshable {

    private final AppContext ctx;

    private final DefaultTableModel model;
    private final JTable table;
    private final JLabel countLabel = new JLabel("Total: 0");

    public ShowAllPage(AppContext ctx) {
        this.ctx = ctx;

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Position"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only table
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel top = UIUtil.paddedPanel(new BorderLayout());
        top.setBorder(BorderFactory.createTitledBorder("All Applicants"));
        top.add(countLabel, BorderLayout.WEST);

        JButton refreshBtn = new JButton("Refresh");
        JButton deleteSelectedBtn = new JButton("Delete Selected");
        JButton backBtn = new JButton("Back to Menu");

        refreshBtn.addActionListener(e -> load());
        deleteSelectedBtn.addActionListener(e -> deleteSelected());
        backBtn.addActionListener(e -> ctx.go(MainFrame.SCREEN_MENU));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(backBtn);
        actions.add(deleteSelectedBtn);
        actions.add(refreshBtn);

        top.add(actions, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // initial load (also called again onShow)
        load();
    }

    @Override
    public void onShow() {
        load();
    }

    private void load() {
        model.setRowCount(0);
        try {
            List<Applicant> list = ctx.db().getAllApplicants();
            for (Applicant a : list) {
                model.addRow(new Object[]{a.id, a.name, a.age, a.post});
            }
            countLabel.setText("Total: " + list.size());
        } catch (SQLException ex) {
            UIUtil.error(this, "Failed to load applicants: " + ex.getMessage());
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            UIUtil.error(this, "Select a row first.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        if (!UIUtil.confirm(this, "Delete applicant with ID " + id + "?")) {
            return;
        }

        try {
            boolean ok = ctx.db().deleteApplicant(id);
            if (ok) {
                UIUtil.info(this, "Deleted.");
                load();
            } else {
                UIUtil.error(this, "No applicant found with that ID.");
            }
        } catch (SQLException ex) {
            UIUtil.error(this, "Delete failed: " + ex.getMessage());
        }
    }
}
