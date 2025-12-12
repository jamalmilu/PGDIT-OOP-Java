package FileOperation;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        //FileWriter writer = new FileWriter("text.txt");
        String filePath = "text.txt";
        //String textContent = "I like bread and butter.\nIt's really good!.\nBuy me bread and butter.";
        String textContent = """
                My life is great,
                I love myself a lot
                I dislike women
                Bye Bye! Everyone.
                """;

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(textContent);
            System.out.println("File has been written.");
        }
        catch(FileNotFoundException e){
            System.out.println("Could not locate file location");
        }
        catch (IOException e) {
            System.out.println("Could not write file.");
        }
    }
}