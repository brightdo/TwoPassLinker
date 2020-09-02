import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class FirstPass extends allDataTraversal {

    private int numOfModules;
    private LinkedHashMap < String, Symbol > symbolTable = new LinkedHashMap < > ();
    private int[] baseAddresses;




    public FirstPass(LinkedList < String > allData) {
        numOfModules = Integer.parseInt(allData.get(0));
        baseAddresses = new int[numOfModules];

        // find count of modules.
        baseAddresses[0] = 0;
        allData.removeFirst();
        //create symbol table and find base addresses
        createSymbolTableAndBaseAddress(allData);
        SecondPass secondpass = new SecondPass(this, allData);
    }


    public void createSymbolTableAndBaseAddress(LinkedList < String > allData) {
        int index = 0;
        int baseAddress = 0;
        int module = 1;
        //find index of where count of symbol definition and use that number to store symbol and its value.
        while (index < allData.size()) {
            int numOfSym = Integer.parseInt((String) allData.get(index));
            int moduleSize = findmoduleSize(allData, index);
            for (int y = 0; y < numOfSym; y++) {
                String variable = allData.get(index + 1 + (2 * y)).toString();

                int addressDef = Integer.parseInt((String) allData.get(index + 2 + (2 * y)));
                int value = addressDef + moduleSize;
                Symbol symbol = new Symbol(variable, value, module - 1);

                //if address definition exceeds size of module.
                if (addressDef > moduleSize) {
                    symbol.setValue(baseAddress);
                    symbol.setExceeds(true);
                }

                if (symbolTable.get(symbol.getName()) == null) {
                    symbolTable.put(variable, symbol);
                } else {
                    symbolTable.get(symbol.getName()).setError(" Error: This variable is multiply defined; first value used.");
                }

            }
            baseAddress += moduleSize;
            // save baseAddress of module while we're at it.
            if (module < numOfModules) {
                baseAddresses[module] = baseAddress;
            }


            //set index to count of symbol definition in next module.
            index += nextModule(allData, index);
            module++;
        }
    }





    // getters and setters
    public HashMap < String, Symbol > getSymbolTable() {
        return symbolTable;
    }


    public void setSymbolTable(LinkedHashMap < String, Symbol > symbolTable) {
        this.symbolTable = symbolTable;
    }


    public int[] getBaseAddresses() {
        return baseAddresses;
    }


    public void setBaseAddresses(int[] baseAddresses) {
        this.baseAddresses = baseAddresses;
    }
    public int getNumOfModules() {
        return numOfModules;
    }


    public void setNumOfModules(int numOfModules) {
        this.numOfModules = numOfModules;
    }




}