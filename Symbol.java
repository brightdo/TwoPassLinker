public class Symbol {
    private String name;
    private boolean exceeds = false;
    private int value;
    private int module;
    private String error = "";


    public Symbol(String sym, int val, int mod) {
        name = sym;
        value = val;
        module = mod;
    }

    //Getters and Setters.
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getValue() {
        return value;
    }


    public void setValue(int value) {
        this.value = value;
    }


    public int getModule() {
        return module;
    }


    public void setModule(int module) {
        this.module = module;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isExceeds() {
        return exceeds;
    }

    public void setExceeds(boolean exceeds) {
        this.exceeds = exceeds;
    }



}