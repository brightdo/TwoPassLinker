
import java.io.*;
import java.util.*;




public class TwoPassLinker {					 								          
	 static HashMap<String, Integer> symTable = new HashMap<String, Integer>(); 		  // Stores symbol table in a hashmap
	 static ArrayList allMemory = new ArrayList();										  // FINAL memory map list that will be displayed
	 static int[] baseAddress; 				  				// Stores base address at a given module
	 static String [][] useTable;	//Stores useTable for each module
	 static String [][] anotherUseTable;
	 static ArrayList symUsed = new ArrayList();
	 static ArrayList symTabArray = new ArrayList();
	static ArrayList countModule = new ArrayList();
	static ArrayList useTabArray = new ArrayList();
	static ArrayList symTabForError = new ArrayList();
	static String errorMessage ="";
	static boolean externalError = false;
	static int numOfMod;
	
	
	// reading file and throwing exception 
	public static Scanner newScanner(String fileName) {
		try{
			Scanner input = new Scanner(new BufferedReader(new FileReader(fileName)));
			return input;
			}
		catch(Exception ex) {
			System.out.printf("Error reading %s\n", fileName);
			System.exit(0);
			}
	return null;
	}
	
	
	
	public static ArrayList createDataList (Scanner input) {
		ArrayList returnData = new ArrayList<String>();
		  String[] indviData = null;
			 //Store string input into an arrayList(allData). Splits all white spaces and does not include empty lines
			 while (input.hasNext()) 
			 {
				 String currentLine = input.next();
				 indviData = currentLine.split("\\s+");			
				 for(int i = 0; i < indviData.length; i++){
					 if(!"".equals(indviData[i])) {					
						 returnData.add(indviData[i]);
					 }
				 }
			 }
		return returnData;
	}
	
	
	
	
	// firstpass of the linker: making symbol table, memory map,and use table 
	public static void firstPass(Scanner input) {
	  
	  ArrayList allData = new ArrayList<String>(); //arrayList with all the data from the input file 
	  
	  allData = createDataList(input); 
		 
//make the symbolTable,Memory Heap, and UseTable while looping through the whole input text file
	int currentMod=0;
	int useTableLine=0;
	int numOfMem=0;
	int numForArray=0;
	int sizeOfModule;
	int numOfSym = Integer.parseInt((String)allData.get(1));
	int lineOfFirstSymUse =(numOfSym*2) + 2;
	int numOfSymInUse = Integer.parseInt((String)allData.get((numOfSym*2) + 2));
	useTableLine=2+(numOfSym*2);
	numOfMod= Integer.parseInt((String)allData.get(0));
	useTable = new String[numOfMod][1000];
	anotherUseTable= new String[numOfMod][1000];
// getting the first symbol table entries
	for (int y=0; y<numOfSym; y++) {
		symTable.put(allData.get(2+(2*y)).toString(), Integer.parseInt((String)allData.get(3+(2*y))));
		symTabForError.add(allData.get(2+(2*y)).toString());
		symTabForError.add(allData.get(3+(2*y)).toString());
		symTabArray.add(allData.get(2+(2*y)).toString());
		countModule.add(allData.get(2+(2*y)).toString());
		}
	// getting the first use table entries
	 	for(int z=0; z<numOfSymInUse; z++) {
	 		useTable[0][z] = allData.get(lineOfFirstSymUse+z+1).toString();
	 		anotherUseTable[0][z] = allData.get(lineOfFirstSymUse+z+1).toString();	
	 		useTabArray.add(allData.get(lineOfFirstSymUse+z+1).toString());
	 		symUsed.add(allData.get(lineOfFirstSymUse+z+1).toString());
	 		
	 }
// start of the loop that reads the use table and symbol table to find the absolute address based on the type (I,A,R,E)
	 	sizeOfModule = Integer.parseInt((String)allData.get( 3 + (2*numOfSym) + (numOfSymInUse)));
		 for(int i = 0; i < allData.size(); i++) 
		 {
			if(allData.get(i).equals("A")||allData.get(i).equals("E")||allData.get(i).equals("I")||allData.get(i).equals("R")) {
			// if address in the word is Absolute
				if(allData.get(i).equals("A")) {
					String errorAddress =  ""+  allData.get(i+1).toString().charAt(1) + allData.get(i+1).toString().charAt(2) + allData.get(i+1).toString().charAt(3);
					int errorInt = Integer.parseInt(errorAddress);
					int finalInt = Integer.parseInt(allData.get(i+1).toString()) - errorInt;
					String finalString = Integer.toString(finalInt) + " Error: Absolute address exceeds machine size; zero used.";
					//error checking 
					if (errorInt > 200) {
						allMemory.add(allData.get(i));
						allMemory.add(finalString);
						}
					else {
						allMemory.add(allData.get(i));
						allMemory.add(allData.get(i+1));
						}
					numOfMem++;
				}
				//if address in the word is Relative  
				else if(allData.get(i).equals("R")) {
					String errorAddress =  ""+  allData.get(i+1).toString().charAt(1) + allData.get(i+1).toString().charAt(2) + allData.get(i+1).toString().charAt(3);
					int errorInt = Integer.parseInt(errorAddress);
					int finalInt = Integer.parseInt(allData.get(i+1).toString()) - errorInt;
					String finalString = Integer.toString(finalInt) + " Error: Relative address exceeds module size; zero used.";
					if (errorInt > sizeOfModule) {
						allMemory.add(allData.get(i));
						allMemory.add(finalString);
						}
					else {
						allMemory.add(allData.get(i));
						allMemory.add(allData.get(i+1));
						}	
					numOfMem++;
				}
				//if address in the word is External  
				else if(allData.get(i).equals("E")) {
					String errorAddress =  ""+  allData.get(i+1).toString().charAt(1) + allData.get(i+1).toString().charAt(2) + allData.get(i+1).toString().charAt(3);
					int errorInt = Integer.parseInt(errorAddress);
					int finalInt = Integer.parseInt(allData.get(i+1).toString());
					anotherUseTable[currentMod][errorInt] = "0";
					String finalString = Integer.toString(finalInt) + " Error: External address exceeds length of use list; treated as immediate.";
					if (errorInt > numOfSymInUse) {
						allMemory.add(allData.get(i));
						allMemory.add(finalString);
						externalError = true;
						}
					else {
						allMemory.add(allData.get(i));
						allMemory.add(allData.get(i+1));
						}
					numOfMem++;
				}
				else {
				allMemory.add(allData.get(i));
				allMemory.add(allData.get(i+1));
				numOfMem++;
				}
				// moving on to the next program and putting the value of symbol table, use table
				if(i<(allData.size()-2)) {
					if(!allData.get(i+2).equals("A")&&!allData.get(i+2).equals("E")&&!allData.get(i+2).equals("I")&&!allData.get(i+2).equals("R")) {
						allMemory.add("0");
						countModule.add("0");
						useTabArray.add("0");
						symUsed.add("0");
						symTabForError.add("null");
						currentMod++;	
						if(!allData.get(i+2).equals("0")) {
							numOfSym = Integer.parseInt((String)allData.get(i+2));
							for(int j=0; j<numOfSym*2; j+=2) {
								int math = Integer.parseInt((String) allData.get(i+j+4))+numOfMem;
								if(symTabArray.isEmpty()) {
									symTable.put(allData.get(i+j+3).toString(), math);	
									symTabForError.add(allData.get(i+j+3).toString());
									symTabForError.add(allData.get(i+j+4).toString());
								}
								for(int h=0; h< symTabArray.size(); h++) {
									if(!symTabArray.get(h).toString().contains(allData.get(i+j+3).toString())) {
										symTable.put(allData.get(i+j+3).toString(), math);
										symTabForError.add(allData.get(i+j+3));
										symTabForError.add(allData.get(i+j+4).toString());
										
									}
								}
								symTabArray.add(allData.get(i+j+3).toString());
								countModule.add(allData.get(i+j+3).toString());
							}
							numOfSymInUse = Integer.parseInt((String)allData.get(i+(numOfSym*2)+3));
							for(int k=0; k<numOfSymInUse; k++) {
								useTable[currentMod][k]=allData.get(i+4+(numOfSym*2)+k).toString();
								anotherUseTable[currentMod][k]=allData.get(i+4+(numOfSym*2)+k).toString();
						 		symUsed.add(allData.get(i+k+(numOfSym*2)+4).toString());
							}
							sizeOfModule = Integer.parseInt((String) allData.get(i+4 + (numOfSym*2) + (numOfSymInUse)));
						}
						else {
							numOfSymInUse = Integer.parseInt((String)allData.get(i+3));
							sizeOfModule = Integer.parseInt((String) allData.get(i+4 + (numOfSym*2) + (numOfSymInUse)));
							for(int x=0; x<numOfSymInUse; x++) {
								useTable[currentMod][x]=allData.get(i+4+(numOfSym*x)).toString();
								anotherUseTable[currentMod][x]=allData.get(i+4+(numOfSym*x)).toString();
								useTabArray.add(allData.get(i+4+(numOfSym*x)).toString());
						 		symUsed.add(allData.get(i+4+(numOfSym*x)).toString());
							}
						}
					}
				}
			}
		} // end of the big loop to find absolute address for everything

	
	int numOfMods = Integer.parseInt((String) allData.get(0));
	int[] sizeOfEachMod = new int[numOfMods];
	baseAddress = new int[numOfMods];
	int countOfMemory=0;
	int countOfArray=0;
	int lastCount = 0;
	baseAddress[countOfArray] = 0;
	int countForError=0;
	int counter=0;
	int anotherCounter=0;
	int nullCounter=0;
	countOfArray =1;	
// finding the size of each program and error handling related to it(If a relative address exceeds the size of the module).
		for( int i =0; i< allMemory.size(); i++) 
		{
			if(!allMemory.get(i).equals("0")&&!allMemory.get(i).equals("R")&&!allMemory.get(i).equals("A")&&!allMemory.get(i).equals("E")&&!allMemory.get(i).equals("I")) {
				countOfMemory++;
			 	}
			 if(allMemory.get(i).equals("0")) {
				baseAddress[countOfArray] = countOfMemory;
				countOfArray++;
				}
			 if(countOfArray == numOfMods)
			 {
				 if(allMemory.get(i).equals("R")||allMemory.get(i).equals("A")||allMemory.get(i).equals("E")||allMemory.get(i).equals("I"))
				 lastCount ++;
			 }
		 }
		for( int i=0; i<baseAddress.length; i++) 
		{
			if(i>0) {
				sizeOfEachMod[counter] = baseAddress[i]-baseAddress[i-1];
				counter++;
				}
		}
		sizeOfEachMod[sizeOfEachMod.length-1] = lastCount;
		for(int i=0; i<symTabForError.size(); i++) 
		{
			if(symTabForError.get(i)!="null"){
				if(Integer.parseInt((String) symTabForError.get(i+1))> sizeOfEachMod[anotherCounter]) 
					{
						errorMessage += "Error: In module" + anotherCounter + " the def of " +symTabForError.get(i) + " exceeds the module size; zero (relative) used\n";
						symTable.put(symTabForError.get(i).toString(), baseAddress[anotherCounter]);
					}
				i++;
			}
			else{
				anotherCounter++;
				}
		}

}// end of first pass


// second pass uses the data from first pass and formats it for the final output (relocating relative addresses and resolving external references.).
 public static void secondPass(Scanner input) {
	int ModuleCount=0;
	int MemoryCount=0;
	int SymTabUseCount=0;
	String valueToAdd;
	System.out.println("+" + ModuleCount );
		for(int i=0; i<allMemory.size();i++){
		 		if(allMemory.get(i).equals("A")) 
		 		{
		 			System.out.println( MemoryCount + "           "  + allMemory.get(i+1));
		 			MemoryCount++;
		 		}
		 		if(allMemory.get(i).equals("I")) 
		 		{
		 			System.out.println( MemoryCount + "           " + allMemory.get(i+1));
		 			MemoryCount++;
		 		}
		 		if(allMemory.get(i).equals("R")) 
		 		{
		 			if(allMemory.get(i+1).toString().contains("Error")) {
		 				System.out.println(MemoryCount + "           " +allMemory.get(i+1));
		 				}
		 			else {
		 			System.out.println(MemoryCount + "           " + (Integer.parseInt((String) allMemory.get(i+1))+baseAddress[ModuleCount]));
		 			MemoryCount++;
		 			}
		 		}
		 		if(allMemory.get(i).equals("E")) 
		 		{
		 			String done="";
		 			valueToAdd = allMemory.get(i+1).toString();
		 			String index = ((Integer.toString(allMemory.get(i+1).toString().charAt(2)-48))+(Integer.toString(allMemory.get(i+1).toString().charAt(3)-48)));
		 			int intIndex = Integer.parseInt(index);
		 			String symTabUse = useTable[ModuleCount][intIndex];
		 			if(!symTable.containsKey(symTabUse)) {
		 				if(!externalError) {
		 				valueToAdd = allMemory.get(i+1).toString() + " Error: " + symTabUse + " is not  defined; zero used.";
		 				}
		 			}
		 			else {
		 			 valueToAdd = symTable.get(symTabUse).toString();
		 			}
		 			for(int j=0; j<4-valueToAdd.length();j++)
		 			{
		 				done += Character.toString(allMemory.get(i+1).toString().charAt(j));
		 			}
		 			done+= valueToAdd;
		 			System.out.println(MemoryCount + "           " +   done);
		 			MemoryCount++;
		 		}
		 		if (allMemory.get(i).equals("0")) 
		 		{
		 			ModuleCount++;
				 	System.out.println("+" + baseAddress[ModuleCount] );
		 			MemoryCount=0;
		 		}
		 	} // end of for loop
}// end of second pass
	 
	 // code for handling errors
public static void errorHandling() {
	System.out.println();
	int module=0;
	int counter=0;
	int counterForUseTab=0;
	ArrayList notUsed = new ArrayList();
	ArrayList notUsedModule = new ArrayList();
		for(int i=0; i<symTabArray.size(); i++){
			if(!symUsed.contains(symTabArray.get(i))) {
		 		notUsed.add(symTabArray.get(i));
		 		}
		 	}
		 if(!notUsed.isEmpty()) {
		 	for(int j=0; j<countModule.size(); j++) {
		 			if(countModule.get(j).toString().equals(notUsed.get(counter))) {
		 				notUsedModule.add(module);
		 				counter++;
		 			}
		 			if (countModule.get(j).toString().equals("0")) {
		 				module ++;
		 			}
		 	}
		 }
		 for(int i=0; i<numOfMod;i++) {
			for (int j=0; j <1000; j++) {
				if(anotherUseTable[i][j]!=null &&!anotherUseTable[i][j].equals("0")) {
							System.out.println("Warning: In Module " + i +" " + anotherUseTable[i][j] + " appeared in the use list but was not actually used.");
				}
			}
		}
		for (int k=0; k <notUsedModule.size(); k++) {
			System.out.println("warning: " + notUsed.get(k) + " was defined in module " + notUsedModule.get(k) + " but never used.");
		}
} // end of error handling

// main()
public static void main(String[] args) throws IOException {
	String filename;
	filename = args[0];
	String val = "";
	Scanner input = newScanner(filename);
	firstPass(input);
	System.out.println("Symbol Table");
	for(int i=0; i<symTabArray.size(); i++) {
		if (val.contains(symTabArray.get(i).toString()))
			{
			val= val.substring(0, val.length()-1);
			val+= " Error:This variable is multiply defined; first value used." + "\n";
			continue;
			}
		val += symTabArray.get(i).toString() + " = " + symTable.get(symTabArray.get(i).toString()).toString() + "\n";
	}
	System.out.println(val);
	System.out.println("             Memory Map");
	secondPass(input);
	errorHandling();
	System.out.println(errorMessage);
	input.close();
}// end of main

}// end of TwoPassLinker class	 

