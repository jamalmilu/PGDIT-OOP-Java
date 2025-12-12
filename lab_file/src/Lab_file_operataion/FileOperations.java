package Lab_file_operataion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.File; // Still needed for the cleanup at the end


public class FileOperations {

    private static final String FILE_NAME = "simple_data.txt";
    private static final Path FILE_PATH = Path.of(FILE_NAME);


    public void writeFile(String content) {
//        System.out.println("--- 1. WRITE Operation ---");
        try {
            // Simplest way to write a String to a file, overwriting existing content.
            Files.writeString(FILE_PATH, content);
//            System.out.println("Content successfully written to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

    public void appendToFile(String content) {
//        System.out.println("--- APPEND Operation ---");
        try {
            // Append content to the file (create if not exists)
            Files.writeString(
                    FILE_PATH,
                    content + System.lineSeparator(),
                    StandardOpenOption.APPEND,
                    StandardOpenOption.CREATE
            );
//            System.out.println("Content successfully appended to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error appending to file: " + e.getMessage());
        }
    }



    public String readFile() {
//        System.out.println("\n--- 2. READ Operation ---");
        try {
            // Simplest way to read the entire file into a single String.
            String content = Files.readString(FILE_PATH);
//            System.out.println("Content successfully read.");
            return content;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return "";
        }
    }


    public void updateFile(String oldString, String newString) {
//        System.out.println("\n--- 3. UPDATE Operation ---");
        try {
            // 1. Read all existing content
            String fileContent = Files.readString(FILE_PATH);

            if (fileContent.contains(oldString)) {
                // 2. Modify the content in memory
                String updatedContent = fileContent.replace(oldString, newString);

                // 3. Write the modified content back (overwrites)
                Files.writeString(FILE_PATH, updatedContent);

//                System.out.println("Successfully updated all occurrences of '" + oldString + "' to '" + newString + "'.");
            } else {
                System.out.println("String '" + oldString + "' not found. No update performed.");
            }
        } catch (IOException e) {
            System.err.println("Error during file update: " + e.getMessage());
        }
    }



}


