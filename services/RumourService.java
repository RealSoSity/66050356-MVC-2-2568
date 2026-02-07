package services;

import java.time.LocalDate;
import java.util.List;

import models.Rumour;
import models.User;
import models.enums.Role;
import models.enums.VerificationStatus;
import repository.ReportRepository;
import repository.RumourRepository;
import repository.UserRepository;

public class RumourService { // Service for managing rumours with business logic
    private final RumourRepository rumourRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public RumourService(RumourRepository rumourRepository, ReportRepository reportRepository,
            UserRepository userRepository) {
        this.rumourRepository = rumourRepository;
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    // List rumours sorted by report count in descending order
    public List<Rumour> listRumoursSortedByReportCountDesc() {
        List<Rumour> rumours = rumourRepository.findAll();
        rumours.sort((a, b) -> Integer.compare(
                reportRepository.findAll().stream().filter(r -> r.getRumourId().equals(b.getRumourId())).toList()
                        .size(),
                reportRepository.findAll().stream().filter(r -> r.getRumourId().equals(a.getRumourId())).toList()
                        .size()));
        return rumours;
    }

    // Get report count for a specific rumour
    public int getReportCount(String rumourId) {
        return reportRepository.findAll().stream().filter(r -> r.getRumourId().equals(rumourId)).toList().size();
    }

    // Get rumour by ID with validation for rumour details
    public Rumour getRumour(String rumourId) {
        Rumour rumour = rumourRepository.findById(rumourId);
        if (rumour == null) {
            throw new IllegalArgumentException("Rumour does not exist");
        }
        return rumour;
    }

    // Verify rumour with business logic
    public void verifyRumour(String checkerId, String rumourId, VerificationStatus status) {
        // Check if Checker user exists
        User checker = userRepository.findById(checkerId);
        if (checker == null) {
            throw new IllegalArgumentException("Checker User does not exist");
        }

        // Check if user has CHECKER role
        if (checker.getRole() != Role.CHECKER) {
            throw new IllegalArgumentException("Only users with CHECKER role can verify rumours");
        }

        Rumour rumour = getRumour(rumourId);

        // Check if rumour is already verified, Users with the CHECKER role can re-verify.
        if (checker.getRole() != Role.CHECKER && rumour.isLockedForReporting()) {
            throw new IllegalArgumentException("Rumour has already been verified");
        }

        rumour.verify(status, checkerId, LocalDate.now());
        rumourRepository.update(rumour);
    }
}
