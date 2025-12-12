package ReadFile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args){
        String filePath = "C:\\Users\\ASUS\\Desktop\\text.txt";

        //BufferedReader reader = new BufferedReader(new FileReader(filePath));

        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
            //System.out.println("That file exits.");
        }
        catch (FileNotFoundException e){
            System.out.println("Could not locate file.");
        }
        catch(IOException e){
            System.out.println("Something went wrong.");
        }
    }
}
