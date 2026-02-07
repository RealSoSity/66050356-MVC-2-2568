package repository.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import models.Rumour;
import repository.RumourRepository;
import util.CsvUtil;

// CRUD operations for Rumour using CSV file
public class CsvRumourRepository implements RumourRepository {
    private final Path filePath;

    public CsvRumourRepository(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    // Get all rumours
    @Override
    public List<Rumour> findAll() {
        List<Rumour> rumours = new ArrayList<>();
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

                // Rumour fields : RumourId, Title, source, CreatedDate, CredibilityScore,
                // status,VerificationStatus, VerifiedDate, VerifiedBy
                
                rumours.add(Rumour.fromCsv(parsed));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
        return rumours;
    }

    // Get rumour by ID
    @Override
    public Rumour findById(String id) {
        List<Rumour> rumours = findAll();
        for (Rumour rumour : rumours) {
            if (rumour.getRumourId().equals(id)) {
                return rumour;
            }
        }
        return null; // Return null if rumour not found
    }

    // Save new rumour by appending to the CSV file
    @Override
    public void save(Rumour rumour) {
        try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
            bw.write(toRow(rumour));
            bw.newLine();
        } catch (Exception e) {
            throw new RuntimeException("Rumour CSV write error", e);
        }
    }

    // Update rumour by replacing the existing entry in the CSV file
    @Override
    public void update(Rumour rumour) {
        List<Rumour> rumours = findAll();
        boolean found = false;
        // Update the rumour in the list
        for (int i = 0; i < rumours.size(); i++) {
            if (rumours.get(i).getRumourId().equals(rumour.getRumourId())) {
                rumours.set(i, rumour);
                found = true;
                break;
            }
        }

        if (!found) { // Rumour not found
            throw new RuntimeException("Rumour with ID " + rumour.getRumourId() + " not found for update.");
        }
        // Rewrite the entire CSV file
        rewriteAll(rumours);
    }

    private void rewriteAll(List<Rumour> rumours) {
        try (BufferedWriter bw = Files.newBufferedWriter(filePath)) {
            // Write header
            bw.write(
                    "RumourId,Title,Source,CreatedDate,CredibilityScore,Status,VerificationStatus,VerifiedBy,VerifiedDate");
            bw.newLine();
            for (Rumour r : rumours) {
                bw.write(toRow(r));
                bw.newLine();
            }
        } catch (Exception e) {
            throw new RuntimeException("Rumour CSV rewrite error", e);
        }
    }

    private String toRow(Rumour r) {
        return String.join(",",
                CsvUtil.escape(r.getRumourId()),
                CsvUtil.escape(r.getTitle()),
                CsvUtil.escape(r.getSource()),
                CsvUtil.escape(r.getCreateDate().toString()),
                CsvUtil.escape(String.valueOf(r.getCredibilityScore())),
                CsvUtil.escape(r.getStatus().name()),
                CsvUtil.escape(r.getVerificationStatus().name()),
                CsvUtil.escape(r.getVerifiedBy() != null ? r.getVerifiedBy() : ""),
                CsvUtil.escape(r.getVerifiedDate() != null ? r.getVerifiedDate().toString() : ""));
    }
}
