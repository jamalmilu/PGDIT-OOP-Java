package HRManagementSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class responsible for reading and writing Applicant data
 * to the backing text file.
 */
public class FileHandler {

    public static final String FILE_NAME = "applicants.txt";
    private static final Path FILE_PATH = Path.of(FILE_NAME);

    /**
     * Reads all applicants from the file. If the file does not exist
     * it will be created and an empty list will be returned.
     */
    public static List<Applicant> readApplicants() {
        List<Applicant> list = new ArrayList<>();

        try {
            // If file does not exist yet, create it and return empty list
            if (!Files.exists(FILE_PATH)) {
                Files.createFile(FILE_PATH);
                return list;
            }

            List<String> lines = Files.readAllLines(FILE_PATH);

            for (String line : lines) {
                if (line == null || line.isBlank()) {
                    continue; // skip empty lines
                }

                String[] parts = line.split(",");
                if (parts.length != 5) {
                    // Malformed line – skip instead of crashing
                    continue;
                }

                String id = parts[0].trim();
                String name = parts[1].trim();
                String ageStr = parts[2].trim();
                String jobPost = parts[3].trim();
                String expStr = parts[4].trim();

                int age;
                int yearsOfExperience;
                try {
                    age = Integer.parseInt(ageStr);
                    yearsOfExperience = Integer.parseInt(expStr);
                } catch (NumberFormatException e) {
                    // Bad numeric data – skip this record
                    continue;
                }

                list.add(new Applicant(id, name, age, jobPost, yearsOfExperience));
            }
        } catch (IOException e) {
            System.err.println("Error reading file '" + FILE_NAME + "': " + e.getMessage());
        }

        return list;
    }

    /**
     * Appends a single applicant to the end of the file.
     */
    public static void appendApplicant(Applicant applicant) {
        try {
            String line = applicant.toString() + System.lineSeparator();
            Files.write(
                    FILE_PATH,
                    line.getBytes(),                     // use platform default charset
                    StandardOpenOption.CREATE,          // create file if missing
                    StandardOpenOption.APPEND           // write at the end
            );
        } catch (IOException e) {
            System.err.println("Error writing to file '" + FILE_NAME + "': " + e.getMessage());
        }
    }

    /**
     * Overwrites the file with the full list of applicants.
     * Used after updates where the in-memory list has changed.
     */
    public static void saveAll(List<Applicant> applicants) {
        List<String> lines = new ArrayList<>();
        for (Applicant a : applicants) {
            lines.add(a.toString());
        }

        try {
            Files.write(
                    FILE_PATH,
                    lines,
                    StandardOpenOption.CREATE,           // create file if missing
                    StandardOpenOption.TRUNCATE_EXISTING // clear previous content
            );
        } catch (IOException e) {
            System.err.println("Error saving file '" + FILE_NAME + "': " + e.getMessage());
        }
    }
}
