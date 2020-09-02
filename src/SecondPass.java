import java.util.LinkedList;

public class SecondPass extends allDataTraversal {
    private FirstPass firstpass;
    private Module[] modules;
    private Error error;
    public SecondPass(FirstPass first, LinkedList < String > allData) {

        firstpass = first;
        modules = new Module[first.getNumOfModules()];
        error = new Error(allData, modules, firstpass.getSymbolTable());
        createModules(first, allData);
        output();
    }

    public void createModules(FirstPass first, LinkedList < String > allData) {
        int index = 0;
        int moduleIndex = 0;
        String[] useTable;
        while (index < allData.size()) {
            int numOfSym = Integer.parseInt((String) allData.get(index));
            for (int y = 0; y < numOfSym; y++) {
                String symbol = allData.get(index + 1 + (2 * y)).toString();

            }
            index += skipSym(allData, index);
            useTable = createUseTable(allData, index);
            Module module = new Module(moduleIndex, useTable, first.getBaseAddresses()[moduleIndex]);
            modules[moduleIndex] = module;
            index += skipUse(allData, index);
            addText(allData, index, modules[moduleIndex]);
            index += skipInst(allData, index);
            index += 1;
            moduleIndex++;
        }
    }

    public void output() {
        int lineNum = 0;
        System.out.println("Symbol Table");
        firstpass.getSymbolTable().forEach((k, v) -> System.out.println(k + "=" + v.getValue() + v.getError()));       
        System.out.println();

        System.out.println("Memory Map");
        for (int i = 0; i < modules.length; i++) {
            for (int j = 0; j < modules[i].getTexts().size(); j++) {
                Text text = (Text) modules[i].getTexts().get(j);
                System.out.println(lineNum + ":  " + text.getAbsoluteAddress(modules[i], firstpass.getSymbolTable()));
                lineNum++;
            }
        }
        System.out.println();
        error.output();
    }

    public String[] createUseTable(LinkedList < String > allData, int index) {
        int numberOfUse = Integer.parseInt(allData.get(index));
        String[] useTable = new String[numberOfUse];
        for (int i = 0; i < numberOfUse; i++) {
            useTable[i] = allData.get(index + i + 1);
        }
        return useTable;
    }

    public void addText(LinkedList < String > allData, int index, Module module) {
        int symbolNum = Integer.parseInt(allData.get(index));
        index += 1;
        for (int i = 0; i < symbolNum * 2; i += 2) {
            Text text = new Text(allData.get(index + i), Integer.parseInt(allData.get(index + i + 1)));
            module.add(text);
        }
    }

}