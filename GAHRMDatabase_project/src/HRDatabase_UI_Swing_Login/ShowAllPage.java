package HRDatabase_UI_Swing_Login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ShowAllPage extends JFrame {

    private final HRDatabase db = AppContext.DB;
    DefaultTableModel tableModel;

    public ShowAllPage() {
        setTitle("All Applicants");
        setSize(500, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Age", "Position"}, 0
        );
        JTable table = new JTable(tableModel);
        loadApplicants();

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            new LandingPage().setVisible(true);
            dispose();
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);
    }

    private void loadApplicants() {
        tableModel.setRowCount(0);
        java.util.List<String[]> list = db.getApplicantsList();
        for (String[] row : list) {
            tableModel.addRow(row);
        }
    }
}
