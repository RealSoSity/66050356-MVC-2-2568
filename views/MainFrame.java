package views;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controllers.ReportController;
import controllers.RumourController;
import controllers.SummaryController;

public class MainFrame extends JFrame { // Main application frame with card layout
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel root = new JPanel(cardLayout);

    // Card names
    public static final String CARD_LIST = "LIST";
    public static final String CARD_DETAIL = "DETAIL";
    public static final String CARD_SUMMARY = "SUMMARY";

    private final RumourListView listView;
    private final RumourDetailView detailView;
    private final SummaryView summaryView;

    public MainFrame(RumourController rumourController, ReportController reportController,
            SummaryController summaryController) {
        super("Rumour Tracking System");
        this.listView = new RumourListView(this, rumourController);
        this.detailView = new RumourDetailView(this, rumourController, reportController);
        this.summaryView = new SummaryView(this, summaryController);

        root.add(listView, CARD_LIST);
        root.add(detailView, CARD_DETAIL);
        root.add(summaryView, CARD_SUMMARY);

        setContentPane(root);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
    }

    public void showList() {
        listView.refresh();
        cardLayout.show(root, CARD_LIST);
    }

    public void showDetail(String rumourId) {
        detailView.loadRumour(rumourId);
        cardLayout.show(root, CARD_DETAIL);
    }

    public void showSummary() {
        summaryView.refresh();
        cardLayout.show(root, CARD_SUMMARY);
    }
}
