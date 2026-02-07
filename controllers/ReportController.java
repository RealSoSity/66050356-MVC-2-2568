package controllers;

import models.enums.ReportType;
import services.ReportService;

public class ReportController {
    private final ReportService reportService;
    
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    public String report(String reporterUserId, String rumourId, ReportType reportType) {
        try {
            reportService.createReport(reporterUserId, rumourId, reportType);
            return "Report submitted successfully.";
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }
}
