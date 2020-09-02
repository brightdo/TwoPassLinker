import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Error {

    private LinkedList < String > allData;
    HashMap < String, Symbol > symbolTable;
    Module[] modules;
    private HashSet < String > used = new HashSet < > ();


    public Error(LinkedList < String > allData, Module[] modules, HashMap < String, Symbol > hashMap) {
        this.allData = allData;
        this.modules = modules;
        this.symbolTable = hashMap;
    }

    public void findSymbolUsed() {
        for (int i = 0; i < modules.length; i++) {
            Module module = modules[i];
            for (int j = 0; j < module.getTexts().size(); j++) {
                Text text = (Text) module.getTexts().get(j);
                if (text.getType().equals("E")) {
                    int useTabRef = (text.getword() % 10);
                    if (useTabRef < module.getUseTable().length)
                        used.add(module.getUseTable()[useTabRef]);
                }
            }
            String[] useTable = module.getUseTable();
            for (int k = 0; k < useTable.length; k++) {
                if (!used.contains(useTable[k])) {
                    System.out.println("Warning: In module " + module.getModuleNum() + " " + useTable[k] + " appeared in the use list but was not actually used.");
                }
            }
        }
    }

    public void findAddressExceedsModuleSize() {
        symbolTable.forEach((k, v)-> {
            if (v.isExceeds()) {
                System.out.println("Error: In module " + v.getModule() + " the def of " + v.getName() + " exceeds the module size; zero (relative) used.");
            }
        });
    }

    public void output() {
        findSymbolUsed();
        System.out.println();
        symbolTable.forEach((k, v)-> {
            if (!used.contains(k))
                System.out.println("Warning: " + v.getName() + " was defined in module " + v.getModule() + " but never used.");
        });
        System.out.println();
        findAddressExceedsModuleSize();
    }
}