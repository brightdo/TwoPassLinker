import java.util.LinkedList;

public class allDataTraversal {

    public int skipSym(LinkedList < String > allData, int index) {
        int numOfSym = Integer.parseInt((String) allData.get(index));
        return (numOfSym * 2) + 1;
    }

    public int skipUse(LinkedList < String > allData, int index) {
        int numOfUse = (Integer.parseInt((String) allData.get(index)));
        return numOfUse + 1;
    }
    public int skipInst(LinkedList < String > allData, int index) {
        int numOfInst = Integer.parseInt((String) allData.get(index));
        return (numOfInst * 2);
    }

    public int nextModule(LinkedList < String > allData, int index) {
        int gap = 0;
        gap += skipSym(allData, index);
        gap += skipUse(allData, index + gap);
        gap += skipInst(allData, index + gap);
        return gap + 1;
    }

    public int findmoduleSize(LinkedList < String > allData, int index) {
        index += skipSym(allData, index);
        index += skipUse(allData, index);
        return Integer.parseInt((String) allData.get(index));
    }
}