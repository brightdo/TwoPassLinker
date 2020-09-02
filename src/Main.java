import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String filename;
        filename = args[0];
        String output = "";
        Scanner input = newScanner(filename);
        LinkedList < String > allData = new LinkedList < String > ();
        allData = createDataList(input);

        FirstPass firstpass = new FirstPass(allData);
        input.close();
    }

    public static Scanner newScanner(String fileName) {
        try {
            Scanner input = new Scanner(new BufferedReader(new FileReader(fileName)));
            return input;
        } catch (Exception ex) {
            System.out.printf("Error reading %s\n", fileName);
            System.exit(0);
        }
        return null;
    }

    public static LinkedList<String> createDataList(Scanner input) {
        LinkedList< String > returnData = new LinkedList < String > ();
        String[] indviData = null;
        //Store string input into an arrayList(allData). Splits all white spaces and removes empty lines
        while (input.hasNext()) {
            String currentLine = input.next();
            indviData = currentLine.split("\\s+");
            for (int i = 0; i < indviData.length; i++) {
                if (!"".equals(indviData[i])) {
                    returnData.add(indviData[i]);
                }
            }
        }
        return returnData;
    }


}
