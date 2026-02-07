package repository;

import java.util.List;

import models.Report;

public interface ReportRepository { // CRUD operations for Report
    List<Report> findAll(); 

    Report findByRumourId(String rumourId);

    // Check if a report exists by user ID and rumour ID, user cannot report the
    // same rumour multiple times
    boolean existsByUserAndRumour(String userId, String rumourId);

    void save(Report report);
}
