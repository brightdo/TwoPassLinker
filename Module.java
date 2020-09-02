import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Module {
    private int moduleNum = 0;
    private String[] useTable;
    private int baseAddress;
    private LinkedList texts;
    private String error = "";

    public Module(int moduleIndex, String[] use, int base) {
        texts = new LinkedList < Text > ();
        moduleNum = moduleIndex;
        useTable = use;
        baseAddress = base;
    }

    public void add(Text text) {
        texts.add(text);
    }



    // getters and setters
    public int getModuleNum() {
        return moduleNum;
    }

    public void setModuleNum(int moduleNum) {
        this.moduleNum = moduleNum;
    }

    public String[] getUseTable() {
        return useTable;
    }

    public void setUseTable(String[] useTable) {
        this.useTable = useTable;
    }

    public int getBaseAddress() {
        return baseAddress;
    }

    public void setBaseAddress(int baseAddress) {
        this.baseAddress = baseAddress;
    }

    public LinkedList getTexts() {
        return texts;
    }

    public void setTexts(LinkedList texts) {
        this.texts = texts;
    }


}