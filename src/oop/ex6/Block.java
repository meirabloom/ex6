package oop.ex6;

import java.util.HashMap;
import java.util.LinkedList;


public abstract class Block {

    Block parent;
    HashMap<String, Variable> localVariables;
    LinkedList<String> lines;
    LinkedList<Variable> globalVariables;
    HashMap<String, MethodBlock> methods;


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
        this.methods = new HashMap<String, MethodBlock>();
        this.globalVariables = new LinkedList<Variable>();
    }

    /**
     * constructor
     * @param parent - the block in which this block is nested
     * @param lines - the lines which make the block
     * @param globalVariables - the variables that are global to this block
     */
    public Block(Block parent, LinkedList<String> lines, LinkedList<String> localVariable,
                 HashMap<String, MethodBlock> methods) throws sJavaException{
        this.parent = parent;
        this.lines = lines;
        VariableFactory factory = new VariableFactory(localVariable, this);
        this. localVariables = factory.getVariables();
        this.methods = methods;

    }

    /**
     * searchs for given param
     * @param varName - variable name
     * @param paramType -
     * @param block
     * @return variable
     */
    Variable searchForVar(String varName){
        Variable searchVar = null;
        Block curBlock = this;
        while(curBlock.parent!=null){
            if(curBlock.localVariables.containsKey(varName)){
                searchVar = curBlock.localVariables.get(varName);
            }
        }
        return searchVar;
    }

    public abstract String getName();

    public String[] getParamNames(){return null;};





}
