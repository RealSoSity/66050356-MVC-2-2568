package repository.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import models.Report;
import models.enums.ReportType;
import repository.ReportRepository;
import util.CsvUtil;
import util.DateUtil;

// CRUD operations for Report using CSV file
public class CsvReportRepository implements ReportRepository {
    private final Path filePath;

    public CsvReportRepository(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    @Override
    public List<Report> findAll() {
        List<Report> reports = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // Skip header line
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                List<String> parsed = CsvUtil.parseLine(line);
                LocalDate reportDate = DateUtil.parseDate(parsed.get(2));
                Report report = new Report(parsed.get(0), parsed.get(1), reportDate, ReportType.valueOf(parsed.get(3)));
                reports.add(report);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV file", e);
        }

        return reports;
    }

    @Override
    public Report findByRumourId(String rumourId) {
        List<Report> reports = findAll();
        for (Report report : reports) {
            if (report.getRumourId().equals(rumourId)) {
                return report;
            }
        }
        return null; // Return null if report not found
    }

    // Check if a report exists by userId and rumourId for preventing duplicate reports
    @Override
    public boolean existsByUserAndRumour(String userId, String rumourId) {
        List<Report> reports = findAll();
        for (Report report : reports) {
            if (report.getReporterUserId().equals(userId) && report.getRumourId().equals(rumourId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void save(Report report) {
        try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
            StringBuilder sb = new StringBuilder();
            sb.append(report.getReporterUserId()).append(",");
            sb.append(report.getRumourId()).append(",");
            sb.append(report.getReportDate().toString()).append(",");
            sb.append(report.getReportType().name()).append("\n");
            bw.write(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error writing to CSV file", e);
        }
    }

}
