package models;

import java.time.LocalDate;

import models.enums.ReportType;

public class Report {
    private final String reporterUserId;
    private final String rumourId;
    private final LocalDate reportDate;
    private final ReportType reportType;

    public Report(String reporterUserId, String rumourId, LocalDate reportDate, ReportType reportType) {
        if (reporterUserId == null || reporterUserId.isEmpty()) {
            throw new IllegalArgumentException("Reporter User ID cannot be null or empty");
        }
        if (rumourId == null || rumourId.isEmpty()) {
            throw new IllegalArgumentException("Rumour ID cannot be null or empty");
        }
        if (reportDate == null) {
            throw new IllegalArgumentException("Report date cannot be null");
        }
        if (reportType == null) {
            throw new IllegalArgumentException("Report type cannot be null");
        }

        this.reporterUserId = reporterUserId;
        this.rumourId = rumourId;
        this.reportDate = reportDate;
        this.reportType = reportType;
    }

    public String getReporterUserId() {
        return reporterUserId;
    }

    public String getRumourId() {
        return rumourId;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public ReportType getReportType() {
        return reportType;
    }

}
