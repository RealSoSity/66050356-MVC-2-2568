package views;

import javax.swing.*;
import java.awt.*;

import controllers.ReportController;
import controllers.RumourController;
import models.Rumour;
import models.enums.ReportType;
import models.enums.VerificationStatus;

public class RumourDetailView extends JPanel {
    private final MainFrame mainFrameNav;
    private final RumourController rumourController;
    private final ReportController reportController;

    // UI fields
    private final JLabel lblRumourId = new JLabel("-");
    private final JLabel lblTitle = new JLabel("-");
    private final JLabel lblSource = new JLabel("-");
    private final JLabel lblCreated = new JLabel("-");
    private final JLabel lblCred = new JLabel("-");
    private final JLabel lblStatus = new JLabel("-");
    private final JLabel lblReports = new JLabel("-");
    private final JLabel lblVerify = new JLabel("-");

    // Action inputs
    private final JTextField txtUserId = new JTextField(12);
    private final JComboBox<ReportType> cbReportType = new JComboBox<>(ReportType.values());

    private final JTextField txtCheckerId = new JTextField(12);
    private final JComboBox<VerificationStatus> cbVerify = new JComboBox<>(
            new VerificationStatus[]{VerificationStatus.TRUE_RUMOUR, VerificationStatus.FALSE_RUMOUR}
    );
    
    private String curRumourId;

    public RumourDetailView(MainFrame mainFrame, RumourController rumourController, ReportController reportController) {
        this.mainFrameNav = mainFrame;
        this.rumourController = rumourController;
        this.reportController = reportController;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(buildTopBar());
        add(buildDetailGrid());
        add(buildActionPanel());
    }

    private JComponent buildTopBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnBack = new JButton("Back to List");
        JButton btnRefresh = new JButton("Refresh");

        btnBack.addActionListener(e -> mainFrameNav.showList());
        btnRefresh.addActionListener(e -> {
            if (curRumourId != null)
                loadRumour(curRumourId);
        });

        p.add(new JLabel("Rumour Detail"));
        p.add(btnBack);
        p.add(btnRefresh);
        return p;
    }

    private JComponent buildDetailGrid() {
        JPanel g = new JPanel(new GridLayout(0, 2, 10, 6));

        g.add(new JLabel("Rumour ID:"));
        g.add(lblRumourId);
        g.add(new JLabel("Title:"));
        g.add(lblTitle);
        g.add(new JLabel("Source:"));
        g.add(lblSource);
        g.add(new JLabel("Created:"));
        g.add(lblCreated);
        g.add(new JLabel("Credibility:"));
        g.add(lblCred);
        g.add(new JLabel("Status:"));
        g.add(lblStatus);
        g.add(new JLabel("Reports:"));
        g.add(lblReports);
        g.add(new JLabel("Verify:"));
        g.add(lblVerify);

        return g;
    }

    private JComponent buildActionPanel() {
        JPanel root = new JPanel(new GridLayout(1, 2, 10, 10));

        // report panel
        JPanel reportP = new JPanel(new GridLayout(0, 2, 8, 6));
        reportP.setBorder(BorderFactory.createTitledBorder("Report This Rumour"));
        JButton btnReport = new JButton("Submit Report");

        btnReport.addActionListener(e -> {
            if (curRumourId == null)
                return;

            String userId = txtUserId.getText().trim();
            ReportType type = (ReportType) cbReportType.getSelectedItem();

            // สำคัญ: View เรียก controller แล้วแสดงผล
            String result = reportController.report(userId, curRumourId, type);
            JOptionPane.showMessageDialog(this, result);

            // after report, refresh to see updated report count
            loadRumour(curRumourId);
        });

        reportP.add(new JLabel("User ID:"));
        reportP.add(txtUserId);
        reportP.add(new JLabel("Type:"));
        reportP.add(cbReportType);
        reportP.add(new JLabel(""));
        reportP.add(btnReport);

        // verify panel
        JPanel verifyP = new JPanel(new GridLayout(0, 2, 8, 6));
        verifyP.setBorder(BorderFactory.createTitledBorder("Verify (CHECKER only)"));
        JButton btnVerify = new JButton("Submit Verification");

        btnVerify.addActionListener(e -> {
            if (curRumourId == null)
                return;

            String checkerId = txtCheckerId.getText().trim();
            VerificationStatus status = (VerificationStatus) cbVerify.getSelectedItem();

            String result = rumourController.verify(checkerId, curRumourId, status);
            JOptionPane.showMessageDialog(this, result);

            // after verify, refresh to see updated verification status
            loadRumour(curRumourId);
        });

        verifyP.add(new JLabel("Checker ID:"));
        verifyP.add(txtCheckerId);
        verifyP.add(new JLabel("Result:"));
        verifyP.add(cbVerify);
        verifyP.add(new JLabel(""));
        verifyP.add(btnVerify);

        root.add(reportP);
        root.add(verifyP);
        return root;
    }

    // Load rumour details into the view
    public void loadRumour(String rumourId) {
        this.curRumourId = rumourId;

        Rumour r = rumourController.getRumourDetails(rumourId);
        int reportCount = rumourController.getReportCount(rumourId);

        lblRumourId.setText(r.getRumourId());
        lblTitle.setText(r.getTitle());
        lblSource.setText(r.getSource());
        lblCreated.setText(r.getCreateDate().toString());
        lblCred.setText(String.valueOf(r.getCredibilityScore()));
        lblStatus.setText(r.getStatus().name());
        lblReports.setText(String.valueOf(reportCount));
        lblVerify.setText(r.getVerificationStatus().name());
    }
}
