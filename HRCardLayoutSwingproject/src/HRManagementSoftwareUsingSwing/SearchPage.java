package HRManagementSoftwareUsingSwing;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class SearchPage extends JPanel {

    private final AppContext ctx;
    private final JTextField idField = new JTextField();
    private final JTextArea resultArea = new JTextArea();

    public SearchPage(AppContext ctx) {
        this.ctx = ctx;

        setLayout(new BorderLayout());

        JPanel top = UIUtil.paddedPanel(new GridBagLayout());
        top.setBorder(BorderFactory.createTitledBorder("Search Applicant"));

        GridBagConstraints c;

        c = UIUtil.gbc(0, 0);
        top.add(new JLabel("Enter ID:"), c);

        c = UIUtil.gbc(1, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        idField.setColumns(12);
        top.add(idField, c);

        JButton searchBtn = new JButton("Search");
        JButton backBtn = new JButton("Back to Menu");

        searchBtn.addActionListener(e -> search());
        backBtn.addActionListener(e -> ctx.go(MainFrame.SCREEN_MENU));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(backBtn);
        actions.add(searchBtn);

        c = UIUtil.gbc(0, 1);
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        top.add(actions, c);

        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        getRootPane().setDefaultButton(searchBtn);
    }

    private void search() {
        String idText = UIUtil.trimmed(idField);
        if (idText.isEmpty()) {
            resultArea.setText("Please enter an ID.");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            resultArea.setText("Invalid ID (must be a number).");
            return;
        }

        try {
            Applicant a = ctx.db().getApplicantById(id);
            if (a == null) {
                resultArea.setText("No record found for ID: " + id);
                return;
            }
            resultArea.setText(
                    "ID: " + a.id +
                    "\nName: " + a.name +
                    "\nAge: " + a.age +
                    "\nPosition: " + a.post
            );
        } catch (SQLException ex) {
            resultArea.setText("Database error: " + ex.getMessage());
        }
    }
}
