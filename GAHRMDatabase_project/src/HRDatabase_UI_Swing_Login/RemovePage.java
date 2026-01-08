package HRDatabase_UI_Swing_Login;

import javax.swing.*;
import java.awt.*;

public class RemovePage extends JFrame {

    private final HRDatabase db = AppContext.DB;
    JTextField idField;

    public RemovePage() {
        setTitle("Remove & Show Applicants");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        idField = new JTextField();
        JButton removeBtn = new JButton("Remove by ID");
        JButton showBtn = new JButton("Show All");
        JButton backBtn = new JButton("Back");

        removeBtn.addActionListener(e -> remove());
        showBtn.addActionListener(e -> {
            new ShowAllPage().setVisible(true);
            dispose();
        });
        backBtn.addActionListener(e -> {
            new LandingPage().setVisible(true);
            dispose();
        });

        setLayout(new GridLayout(5, 1, 5, 5));
        add(new JLabel("Enter ID to remove:"));
        add(idField);
        add(removeBtn);
        add(showBtn);
        add(backBtn);
    }

    private void remove() {
        try {
            int id = Integer.parseInt(idField.getText());
            if (!UIUtil.confirm(this, "Are you sure you want to delete ID " + id + "?")) {
                return;
            }
            db.deleteApplicant(id);
            UIUtil.showInfo(this, "Applicant Deleted!");
            idField.setText("");

        } catch (Exception e) {
            UIUtil.showError(this, "Invalid ID!");
        }
    }
}
