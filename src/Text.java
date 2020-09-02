import java.util.HashMap;

public class Text {
    private String type;
    private int word;
    private String error = "";

    public Text(String t, int a) {
        type = t;
        word = a;
    }


    public String getAbsoluteAddress(Module module, HashMap < String, Symbol > symbolTable) {
        error = "";
        int absAddress = word;
        String[] useTable = module.getUseTable();
        if (type.equals("R")) {
            int useTabRef = (word % 1000);
            if (useTabRef > module.getTexts().size()) {
                absAddress -= useTabRef;
                error += " Error: Relative address exceeds module size; zero used.";
            } else
                absAddress += module.getBaseAddress();
        } else if (type.equals("E")) {
            int useTabRef = (word % 10);
            int add = 0;
            if (useTabRef > module.getUseTable().length) {
                return absAddress + " Error: External address exceeds length of use list; treated as immediate.";
            }
            if (symbolTable.get(module.getUseTable()[useTabRef]) != null) {
                add = symbolTable.get(useTable[useTabRef]).getValue();
            } else {
                error += " Error: " + useTable[useTabRef] + " is not defined; zero used.";
            }
            absAddress -= useTabRef;
            absAddress += add;
        } else if (type.equals("A")) {
            int variable = (word % 1000);
            if (variable > 200) {
                absAddress -= variable;
                error += " Error: Absolute address exceeds machine size; zero used.";
            }
        }

        return absAddress + error;
    }



    // getters and setters	
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getword() {
        return word;
    }

    public void setword(int word) {
        this.word = word;
    }
}