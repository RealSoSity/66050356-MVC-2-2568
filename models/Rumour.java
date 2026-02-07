package models;

import java.time.LocalDate;
import java.util.List;

import models.enums.RumourStatus;
import models.enums.VerificationStatus;

public class Rumour {
    private final String rumourId;
    private String title;
    private String source;
    private final LocalDate createDate;
    private double credibilityScore;
    private RumourStatus status;

    // Summary (True Rumour, Fals Rumour)
    private VerificationStatus verificationStatus;
    private String verifiedBy;
    private LocalDate verifiedDate;

    public Rumour(String rumourId, String title, String source, LocalDate createDate, double credibilityScore,
            RumourStatus status, VerificationStatus verificationStatus, String verifiedBy, LocalDate verifiedDate) {
        if (rumourId == null || !rumourId.matches("^[1-9][0-9]{7}$")) {
            throw new IllegalArgumentException("Rumour ID must be 8 digits and cannot start with 0");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (source == null || source.isEmpty()) {
            throw new IllegalArgumentException("Source cannot be null or empty");
        }
        if (createDate == null) {
            throw new IllegalArgumentException("Create date cannot be null");
        }
        if (credibilityScore < 0 || credibilityScore > 100) {
            throw new IllegalArgumentException("Credibility score must be between 0 and 100");
        }
        if (status == null) {
            throw new IllegalArgumentException("Rumour status cannot be null");
        }
        if (verificationStatus == null) {
            throw new IllegalArgumentException("Verification status cannot be null");
        }

        this.rumourId = rumourId;
        this.title = title;
        this.source = source;
        this.createDate = createDate;
        this.credibilityScore = credibilityScore;
        this.status = status;
        this.verificationStatus = verificationStatus;
        this.verifiedBy = verifiedBy;
        this.verifiedDate = verifiedDate;
    }

    public String getRumourId() {
        return rumourId;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public double getCredibilityScore() {
        return credibilityScore;
    }

    public RumourStatus getStatus() {
        return status;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public LocalDate getVerifiedDate() {
        return verifiedDate;
    }

    public void setStatus(RumourStatus status) {
        this.status = status;
    }

    public static Rumour fromCsv(List<String> p) {
        String verifiedBy = p.get(7).trim().isEmpty() ? null : p.get(7).trim();
        LocalDate verifiedDate = p.get(8).trim().isEmpty() ? null : LocalDate.parse(p.get(8).trim());

        return new Rumour(
                p.get(0).trim(),
                p.get(1),
                p.get(2),
                LocalDate.parse(p.get(3).trim()),
                Double.parseDouble(p.get(4).trim()),
                RumourStatus.valueOf(p.get(5).trim()),
                VerificationStatus.valueOf(p.get(6).trim()),
                verifiedBy,
                verifiedDate);
    }

    // Rumour is locked for reporting if it has been verified
    public boolean isLockedForReporting() {
        return verificationStatus != VerificationStatus.UNVERIFIED;
    }

    // Method to verify the rumour
    public void verify(VerificationStatus status, String verifiedBy, LocalDate verifiedDate) {
        this.verificationStatus = status;
        this.verifiedBy = verifiedBy;
        this.verifiedDate = verifiedDate;
    }
}
