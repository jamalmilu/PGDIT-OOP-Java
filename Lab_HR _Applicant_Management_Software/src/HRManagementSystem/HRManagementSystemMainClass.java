package HRManagementSystem;

import java.util.List;
import java.util.Scanner;

/**
 * Console-based HR management system for Applicants.
 * Uses FileHandler to persist data to a text file.
 */
public class HRManagementSystemMainClass {

    public static void main(String[] args) {
        // Use try-with-resources so Scanner is closed automatically on exit
        try (Scanner scanner = new Scanner(System.in)) {

            // Load existing applicants from file (if any)
            List<Applicant> applicants = FileHandler.readApplicants();

            // Main menu loop
            while (true) {
                printMenu();
                int choice = getIntInput(scanner, 1, 5);

                switch (choice) {
                    case 1 -> displayAll(applicants);
                    case 2 -> search(applicants, scanner);
                    case 3 -> add(applicants, scanner);
                    case 4 -> update(applicants, scanner);
                    case 5 -> {
                        System.out.println("Exiting...");
                        return; // Exit the program
                    }
                }
            }
        }
    }

    // Prints the main menu options
    private static void printMenu() {
        System.out.println("\n===== HR Applicant Management System =====");
        System.out.println("1. Display All Applicants");
        System.out.println("2. Search Applicant by ID");
        System.out.println("3. Add New Applicant");
        System.out.println("4. Update Applicant Information");
        System.out.println("5. Exit");
        System.out.print("Choose option: ");
    }

    // ----------------- CORE FEATURES -----------------

    // Display all applicants
    public static void displayAll(List<Applicant> list) {
        if (list.isEmpty()) {
            System.out.println("No applicants available.");
            return;
        }

        System.out.println("\n--- Applicant List ---");
        for (Applicant a : list) {
            System.out.println(a.display());
        }
    }

    // Search applicant by ID
    public static void search(List<Applicant> list, Scanner sc) {
        System.out.print("Enter Applicant ID: ");
        String id = sc.nextLine().trim();

        for (Applicant a : list) {
            if (a.getApplicantId().equalsIgnoreCase(id)) {
                System.out.println("Applicant Found:\n" + a.display());
                return;
            }
        }
        System.out.println("Applicant not found.");
    }

    // Add a new applicant with validation and ID uniqueness check
    public static void add(List<Applicant> list, Scanner sc) {
        String id;

        // Ask until we get a non-empty and unique ID
        while (true) {
            System.out.print("Enter ID: ");
            id = sc.nextLine().trim();

            if (id.isEmpty()) {
                System.out.println("ID cannot be empty.");
                continue;
            }

            if (idExists(list, id)) {
                System.out.println("Error: ID already exists. Enter a different ID.");
            } else {
                break;
            }
        }

        System.out.print("Enter Name: ");
        String name = getNonEmptyText(sc);

        System.out.print("Enter Age: ");
        int age = getPositiveInt(sc);

        System.out.print("Enter Job Post: ");
        String job = getNonEmptyText(sc);

        System.out.print("Enter Years of Experience: ");
        int exp = getPositiveInt(sc);

        Applicant newApplicant = new Applicant(id, name, age, job, exp);
        list.add(newApplicant);                   // update in-memory list
        FileHandler.appendApplicant(newApplicant); // append to file

        System.out.println("Applicant added successfully.");
    }

    // Update an existing applicant (if found) with validation
    public static void update(List<Applicant> list, Scanner sc) {
        System.out.print("Enter Applicant ID to update: ");
        String id = sc.nextLine().trim();

        for (Applicant a : list) {
            if (a.getApplicantId().equalsIgnoreCase(id)) {

                System.out.print("Enter new Name: ");
                a.setName(getNonEmptyText(sc));

                System.out.print("Enter new Age: ");
                a.setAge(getPositiveInt(sc));

                System.out.print("Enter new Job Post: ");
                a.setJobPost(getNonEmptyText(sc));

                System.out.print("Enter new Years of Experience: ");
                a.setYearsOfExperience(getPositiveInt(sc));

                // Persist full updated list to file
                FileHandler.saveAll(list);
                System.out.println("Applicant updated successfully.");
                return;
            }
        }

        System.out.println("Applicant not found.");
    }

    // -------- INPUT VALIDATION HELPERS ----------

    /**
     * Reads an integer between min and max (inclusive) from the scanner.
     * Keeps asking until the user enters a valid value.
     */
    private static int getIntInput(Scanner sc, int min, int max) {
        while (true) {
            try {
                int num = Integer.parseInt(sc.nextLine().trim());
                if (num < min || num > max) {
                    System.out.print("Choose a number between " + min + " and " + max + ": ");
                } else {
                    return num;
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
    }

    /**
     * Reads a positive (or zero) integer from the scanner.
     */
    private static int getPositiveInt(Scanner sc) {
        while (true) {
            try {
                int num = Integer.parseInt(sc.nextLine().trim());
                if (num < 0) {
                    System.out.print("Enter a positive number: ");
                } else {
                    return num;
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Enter again: ");
            }
        }
    }

    /**
     * Reads a non-empty line of text from the scanner.
     */
    private static String getNonEmptyText(Scanner sc) {
        while (true) {
            String text = sc.nextLine().trim();
            if (text.isEmpty()) {
                System.out.print("Field cannot be empty. Enter again: ");
            } else {
                return text;
            }
        }
    }

    /**
     * Returns true if an applicant with the given ID already exists.
     */
    private static boolean idExists(List<Applicant> list, String id) {
        for (Applicant a : list) {
            if (a.getApplicantId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }
}
