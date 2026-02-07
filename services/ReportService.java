package services;

import java.time.LocalDate;

import models.Report;
import models.User;
import models.enums.ReportType;
import models.enums.RumourStatus;
import repository.ReportRepository;
import repository.RumourRepository;
import repository.UserRepository;

public class ReportService { // Service for creating reports with business logic
    private final int PANIC_THRESHOLD;
    private final ReportRepository reportRepository;
    private final RumourRepository rumourRepository;
    private final UserRepository userRepository;

    public ReportService(int panicThreshold, ReportRepository reportRepository, RumourRepository rumourRepository,
            UserRepository userRepository) {
        this.PANIC_THRESHOLD = panicThreshold;
        this.reportRepository = reportRepository;
        this.rumourRepository = rumourRepository;
        this.userRepository = userRepository;
    }

    // Create Report for a Rumour accourding with Business Logic
    // Business Logic: 1. user can report 1 rumour only once, 2. verified rumours
    // cannot be reported, 3. ReportCount >= PANIC_THRESHOLD => Rumour status =
    // PANIC.
    public void createReport(String reporterUserId, String rumourId, ReportType reportType) {
        // Check if user exists
        if (userRepository.findById(reporterUserId) == null) {
            throw new IllegalArgumentException("Reporter User ID does not exist");
        }

        // Check if user exists
        User user = userRepository.findById(reporterUserId);
        if (user == null) {
            throw new IllegalArgumentException("User does not exist");
        }

        // Check if rumour exists
        models.Rumour rumour = rumourRepository.findById(rumourId);
        if (rumour == null) {
            throw new IllegalArgumentException("Rumour ID does not exist");
        }

        // Check if rumour is verified
        if (rumour.isLockedForReporting()) {
            throw new IllegalArgumentException("Verified rumours cannot be reported");
        }

        // Check for one-time report by the same user
        if (reportRepository.existsByUserAndRumour(reporterUserId, rumourId)) {
            throw new IllegalArgumentException("User has already reported this rumour");
        }

        // Create and save the report
        Report report = new Report(reporterUserId, rumourId, LocalDate.now(), reportType);
        reportRepository.save(report);

        // Check if report count exceeds PANIC_THRESHOLD to update rumour status
        int reportCount = reportRepository.findAll().stream().filter(r -> r.getRumourId().equals(rumourId)).toList()
                .size(); // Count reports for the rumour
        if (reportCount >= PANIC_THRESHOLD) {
            rumour.setStatus(RumourStatus.PANIC);
            rumourRepository.update(rumour);
        }
    }
}
