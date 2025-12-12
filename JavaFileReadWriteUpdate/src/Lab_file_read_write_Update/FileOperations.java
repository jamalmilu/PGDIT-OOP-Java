package Lab_file_read_write_Update;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;

public class FileOperations {

    private static final String FILE_NAME = "allData/simple_data.txt";
    private static final Path FILE_PATH = Path.of(FILE_NAME);

    // Create/overwrite file with header
    public static void writeFile() {
        //System.out.println("--- INIT FILE (WRITE HEADER) ---");
        String header = "Name, roll, semester, course_id, marks" + System.lineSeparator();
        try {
            // Overwrite existing file with header
            Files.writeString(FILE_PATH, header);
            System.out.println("Header written to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error writing header: " + e.getMessage());
        }
    }

    // Append a single student record
    public static void appendRecord(String name,
                                    String roll,
                                    String semester,
                                    String courseId,
                                    String marks) {
        //System.out.println("--- APPEND RECORD ---");
        String line = String.format(
                "%s, %s, %s, %s, %s%s",
                name, roll, semester, courseId, marks, System.lineSeparator()
        );

        try {
            Files.writeString(
                    FILE_PATH,
                    line,
                    StandardOpenOption.APPEND,
                    StandardOpenOption.CREATE
            );
            System.out.println("Record appended for roll " + roll);
        } catch (IOException e) {
            System.err.println("Error appending record: " + e.getMessage());
        }
    }

    // Read whole file as String
    public static String readFile() {
        //System.out.println("\n--- READ FILE ---");
        try {
            String content = Files.readString(FILE_PATH);
            System.out.println("File read successfully.");
            return content;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return "";
        }
    }

    /**
     * Update course_id and marks for the given roll.
     * File format:
     * Name, roll, semester, course_id, marks
     */
    public static void updateCourseAndMarks(String roll,
                                            String newCourseId,
                                            String newMarks) {
        //System.out.println("\n--- UPDATE COURSE & MARKS BY ROLL ---");
        try {
            List<String> lines = Files.readAllLines(FILE_PATH);
            boolean updated = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (i == 0 || line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 5) {
                    continue;
                }

                String currentRoll = parts[1].trim();
                if (currentRoll.equals(roll)) {
                    String name = parts[0].trim();
                    String semester = parts[2].trim();

                    String updatedLine = String.format(
                            "%s, %s, %s, %s, %s",
                            name,
                            currentRoll,
                            semester,
                            newCourseId.trim(),
                            newMarks.trim()
                    );

                    lines.set(i, updatedLine);
                    updated = true;
                    break;
                }
            }

            if (updated) {
                Files.write(FILE_PATH, lines);
                System.out.println("Updated roll " + roll +
                        " with course_id=" + newCourseId +
                        " and marks=" + newMarks);
            } else {
                System.out.println("Roll " + roll + " not found. No update performed.");
            }

        } catch (IOException e) {
            System.err.println("Error while updating file: " + e.getMessage());
        }
    }
}