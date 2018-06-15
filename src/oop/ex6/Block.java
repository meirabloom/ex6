package oop.ex6;

import java.util.HashMap;
import java.util.LinkedList;


public abstract class Block {

    Block parent;
    HashMap<String, Variable> localVariables;
    LinkedList<String> lines;

    /**
     * a constructor of a parentless block (the global block)
     */
    public Block(LinkedList<String> lines){ //TODO deal with global variables
        this.parent = null;
        this.lines = lines;
    }

    /**
     * constructor
     * @param parent - the block in which this block is nested
     * @param lines - the lines which make the block
     * @param globalVariables - the variables that are global to this block
     */
    public Block(Block parent, LinkedList<String> lines, LinkedList<String> globalVariables){
        this.parent = parent;
        VariableFactory varFactory = new VariableFactory(globalVariables);
        localVariables = varFactory.getVariables();
        this.lines = lines;
    }

    /**
     * searches the block for the its local variables. if the variable name is already assigned to a global
     * variable, an exception is thrown
     */
    public abstract void setVariables() throws sJavaException;




}
