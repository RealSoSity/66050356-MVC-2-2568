package repository.csv;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import models.User;
import models.enums.Role;
import repository.UserRepository;
import util.CsvUtil;

// CRUD operations for User using CSV file
public class CsvUserRepository implements UserRepository {
    private final Path filePath;

    public CsvUserRepository(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

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
                String id = parsed.get(0);
                String name = parsed.get(1);
                Role role = Role.valueOf(parsed.get(2));
                User user = new User(id, name, role);
                users.add(user);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV file", e);
        }

        return users;
    }

    @Override
    public User findById(String id) {
        List<User> users = findAll();
        for (User user : users) {
            if(user.getUserId().equals(id)) {
                return user;
            }
        }
        return null; // Return null if user not found
    }
}
