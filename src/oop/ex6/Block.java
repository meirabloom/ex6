package oop.ex6;

import java.util.HashMap;
import java.util.LinkedList;


public abstract class Block {

    Block parent;
    HashMap<String, Variable> localVariables;
    LinkedList<String> lines;
    LinkedList<Variable> globalVariables;
    LinkedList<String> methods;


    //constructors

    /**
     * default constructor
     */
    Block(){}

    /**
     * a constructor of a parentless block (the global block)
     */
    public Block(LinkedList<String> lines){ //TODO deal with global variables
        this.parent = null;
        this.lines = lines;
        this.methods = new LinkedList<String>();
        this.globalVariables = new LinkedList<Variable>();
    }

    /**
     * constructor
     * @param parent - the block in which this block is nested
     * @param lines - the lines which make the block
     * @param globalVariables - the variables that are global to this block
     */
    public Block(Block parent, LinkedList<String> lines,
                 LinkedList<String> methods, LinkedList<String> localVariable) throws sJavaException{
        this.parent = parent;
        this.lines = lines;
        VariableFactory factory = new VariableFactory(localVariable, globalVariables);
        this. localVariables = factory.getVariables();
        this.methods = methods;

    }

    public abstract String getName();



}
