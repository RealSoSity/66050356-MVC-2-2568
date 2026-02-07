package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import controllers.SummaryController;
import models.Rumour;

public class SummaryView extends JPanel { // View for displaying summary of rumours
    private final MainFrame mainFrameNav;
    private final SummaryController controller;

    private final DefaultTableModel panicModel;
    private final DefaultTableModel verifiedModel;

    public SummaryView(MainFrame mainFrame, SummaryController summaryController) {
        this.mainFrameNav = mainFrame;
        this.controller = summaryController;

        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("Back to List");
        JButton refreshButton = new JButton("Refresh");
        backButton.addActionListener(e -> mainFrameNav.showList());
        refreshButton.addActionListener(e -> refresh());

        top.add(backButton);
        top.add(refreshButton);
        add(top, BorderLayout.NORTH);

        panicModel = new DefaultTableModel(
                new Object[] { "Rumour ID", "Title", "Source", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        verifiedModel = new DefaultTableModel(
                new Object[] { "Rumour ID", "Title", "Verify", "By", "Verified Date" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable panicTable = new JTable(panicModel);
        JTable verifiedTable = new JTable(verifiedModel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,

                wrap("Panic Rumours", new JScrollPane(panicTable)),
                wrap("Verified Rumours (TRUE/FALSE)", new JScrollPane(verifiedTable)));
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel wrap(String title, JComponent comp) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(comp, BorderLayout.CENTER);
        return panel;
    }

    // Refresh the summary tables
    public void refresh() {
        panicModel.setRowCount(0);
        verifiedModel.setRowCount(0);

        for (Rumour r : controller.panicRumours()) {
            panicModel.addRow(new Object[] { r.getRumourId(), r.getTitle(), r.getSource(), r.getStatus().name() });
        }
        for (Rumour r : controller.verifiedRumours()) {
            verifiedModel.addRow(new Object[] {
                    r.getRumourId(),
                    r.getTitle(),
                    r.getVerificationStatus().name(),
                    r.getVerifiedBy() == null ? "-" : r.getVerifiedBy(),
                    r.getVerifiedDate() == null ? "-" : r.getVerifiedDate().toString()
            });
        }
    }

}
