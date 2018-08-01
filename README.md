Two-Pass-Linker
===============
**Miguel Amigot**
<br>
*Operating Systems, Spring 2018*

Implementation of a two-pass linker in Java for a target machine that's word addressable with a memory of 200 words, each consisting of 4 decimal digits.  
The first (leftmost) digit is the opcode, which is unchanged by the linker. The remaining three digits (called the address
field) are either  
• An immediate operand, which your program does not change.  
• An absolute address, which your program does not change.  
• A relative address, which your program relocates.  
• An external address, which your program resolves.  
  
Input consists of a series of object modules, each of which contains three lists: definitions of symbols that may be
used in other modules, uses in this module of symbols defined in other modules, and the program text.
  
### Compiling
```
javac TwoPassLinker.java
```

### Running
Provide the text-based input as the first and only command line argument in the following way. See sample [input files](/input) as well as their [corresponding outputs](/output).
```
java TwoPass input(1~9).txt
```
