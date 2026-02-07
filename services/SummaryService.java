package services;

import java.util.List;

import models.Rumour;
import models.enums.RumourStatus;
import models.enums.VerificationStatus;
import repository.RumourRepository;

public class SummaryService { // Service for generating summaries
    private final RumourRepository rumourRepository;

    public SummaryService(RumourRepository rumourRepository) {
        this.rumourRepository = rumourRepository;
    }

    // Get all rumours with PANIC status
    public List<Rumour> getPanicRumours() {
        return rumourRepository.findAll().stream()
                .filter(r -> r.getStatus() == RumourStatus.PANIC)
                .toList();
    }

    // Get all verified rumours
    public List<Rumour> getVerifiedRumours() {
        return rumourRepository.findAll().stream()
                .filter(r -> r.getVerificationStatus() != VerificationStatus.UNVERIFIED)
                .toList();
    }
}
