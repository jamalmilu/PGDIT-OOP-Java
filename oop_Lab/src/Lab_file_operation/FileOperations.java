package Lab_file_operation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileOperations {
    // constants
    private static final String FILE_NAME = "myFile.txt";
    private static final Path FILE_PATH = Path.of(FILE_NAME);

    // methods for write file (overwrites existing content)
    public void writeFile(String content){
        System.out.println("=== Write operation ===");
        try {
            // Overwrites the file with the new content
            Files.writeString(FILE_PATH, content );
            System.out.println("Content successfully written to " + FILE_NAME);
        }catch(IOException e){
            System.err.println("Error writing file "+ e.getMessage());
        }
    }

    // methods for append file
    public void appendToFile(String content){
        System.out.println("\n=== Append operation ===");
        try{
            // Append content to the file (creates if not exists)
            Files.writeString(
                    FILE_PATH,
                    content + System.lineSeparator(),
                    StandardOpenOption.APPEND,
                    StandardOpenOption.CREATE
            );
            System.out.println("Content successfully appended to " + FILE_NAME);
        } catch(IOException e){
            System.err.println("Error appending file "+ e.getMessage());
        }
    }

    // methods for read file
    public String readFile(){
        System.out.println("\n=== Read operation ===");
        try{
            String content = Files.readString(FILE_PATH);
            System.out.println("Content successfully read from " + FILE_NAME + ".");
            return content;
        } catch(IOException e){
            System.err.println("Error reading file: " + e.getMessage());
            return null; // Return null on error
        }
    }

    // methods for update file
    public void updateFile(String oldString, String newString){
        System.out.println("\n=== Update operation === ");
        try {
            String fileContent = Files.readString(FILE_PATH);
            if(fileContent.contains(oldString)){
                String updatedContent = fileContent.replace(oldString, newString);
                // Write the modified content back (overwrite)
                Files.writeString(FILE_PATH, updatedContent);
                System.out.println("Successfully updated all occurrences of '" + oldString + "' to '" + newString + "'.");
            } else {
                System.out.println("String '"+ oldString +"' is not found. No update performed.");
            }
        } catch(IOException e){
            System.err.println("Error during file update: "+ e.getMessage());
        }
    }
}