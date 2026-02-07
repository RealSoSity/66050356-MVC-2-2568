import javax.swing.SwingUtilities;

import controllers.ReportController;
import controllers.RumourController;
import controllers.SummaryController;
import repository.ReportRepository;
import repository.RumourRepository;
import repository.UserRepository;
import repository.csv.CsvReportRepository;
import repository.csv.CsvRumourRepository;
import repository.csv.CsvUserRepository;
import services.ReportService;
import services.RumourService;
import services.SummaryService;
import views.MainFrame;

public class Main {

    public static void main(String[] args) {
       //Initialize repositories
        RumourRepository rumourRepository = new CsvRumourRepository("data/rumours.csv");
        UserRepository userRepository = new CsvUserRepository("data/users.csv");
        ReportRepository reportRepository = new CsvReportRepository("data/reports.csv");

        // Panic threshold for reports.
        int PANIC_THRESHOLD = 3;

        //Initialize Services
        ReportService reportService = new ReportService(PANIC_THRESHOLD, reportRepository, rumourRepository, userRepository);
        RumourService rumourService = new RumourService(rumourRepository, reportRepository, userRepository);
        SummaryService summaryService = new SummaryService(rumourRepository);

        //Initialize Controllers
        ReportController reportController = new ReportController(reportService);
        RumourController rumourController = new RumourController(rumourService);
        SummaryController summaryController = new SummaryController(summaryService);

        //Initialize Main Frame
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(rumourController, reportController, summaryController);
            mainFrame.setVisible(true);
        });
        

    }
}
