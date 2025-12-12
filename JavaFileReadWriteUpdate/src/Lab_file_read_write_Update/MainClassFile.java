package Lab_file_read_write_Update;

import java.util.Scanner;

public class MainClassFile {
    public static void main(String[] args) {
        FileOperations fileOperations = new FileOperations();
        Scanner scanner = new Scanner(System.in);

        // 1. Initialize file with header
        fileOperations.writeFile();

        // 2. Take input from user for multiple students
        System.out.print("How many student records do you want to enter? ");
        int n = Integer.parseInt(scanner.nextLine().trim());

        for (int i = 1; i <= n; i++) {
            System.out.println("\n--- Enter data for student #" + i + " ---");

            System.out.print("Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Roll: ");
            String roll = scanner.nextLine().trim();

            System.out.print("Semester: ");
            String semester = scanner.nextLine().trim();

            System.out.print("Course ID: ");
            String courseId = scanner.nextLine().trim();

            System.out.print("Marks: ");
            String marks = scanner.nextLine().trim();

            fileOperations.appendRecord(name, roll, semester, courseId, marks);
        }

        // 3. Show current file content
        String currentContent = fileOperations.readFile();
        System.out.println("\n[Current File Content]\n" + currentContent);

        // 4. Update course_id and marks for a specific roll (from user input)
        System.out.print("\nEnter roll to update: ");
        String rollToUpdate = scanner.nextLine().trim();

        System.out.print("Enter new Course ID: ");
        String newCourseId = scanner.nextLine().trim();

        System.out.print("Enter new Marks: ");
        String newMarks = scanner.nextLine().trim();

        fileOperations.updateCourseAndMarks(rollToUpdate, newCourseId, newMarks);

        // 5. Show file content after update
        String updatedContent = fileOperations.readFile();
        System.out.println("\nFile Content After Update\n" + updatedContent);

        scanner.close();
    }
}

