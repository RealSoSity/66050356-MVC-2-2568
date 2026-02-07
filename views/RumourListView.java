package views;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import controllers.RumourController;
import models.Rumour;

public class RumourListView extends JPanel {
    private final MainFrame mainFrameNav;
    private final RumourController rumourController;

    private final DefaultTableModel tableModel;
    private final JTable table;

    public RumourListView(MainFrame mainFrameNav, RumourController rumourController) {
        this.mainFrameNav = mainFrameNav;
        this.rumourController = rumourController;

        setLayout(new BorderLayout(10, 10));
        add(buildTopBar(), BorderLayout.NORTH);

        // Table setup for displaying rumours
        tableModel = new DefaultTableModel(
                new Object[] { "Rumour ID", "Title", "Source", "Created", "Credibility", "Reports", "Status" }, 0) {
            // isCellEditable override to make cells non-editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);
    }

    // Build top bar with title and buttons
    private JComponent buildTopBar(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel("Rumour List (Sorted by Reports)");
        JButton refreshButton = new JButton("Refresh");
        JButton summaryButton = new JButton("Go to Summary");

        refreshButton.addActionListener(e -> refresh());
        summaryButton.addActionListener(e -> mainFrameNav.showSummary());
        panel.add(title);
        panel.add(refreshButton);
        panel.add(summaryButton);
        return panel;
    }

    // Build bottom bar with "Open Detail" button
    private JComponent buildBottomBar(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton openDetailButton = new JButton("Open Detail");
        openDetailButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row < 0){
                JOptionPane.showMessageDialog(this, "Please select a rumour row");
                return;
            }
            String rumourId = (String) tableModel.getValueAt(row, 0);
            mainFrameNav.showDetail(rumourId);
        });
        panel.add(openDetailButton);
        return panel;
    }

    // Refresh the table data
    public void refresh(){
         tableModel.setRowCount(0); // Clear existing rows
         List<Rumour> rumours = rumourController.listRumours();
         for(Rumour rumour : rumours){
             Object[] row = new Object[]{
                 rumour.getRumourId(),
                 rumour.getTitle(),
                 rumour.getSource(),
                 rumour.getCreateDate(),
                 rumour.getCredibilityScore(),
                 rumourController.getReportCount(rumour.getRumourId()),
                 rumour.getStatus()
             };
             tableModel.addRow(row);
         }
    }
}
