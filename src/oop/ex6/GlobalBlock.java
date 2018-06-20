package oop.ex6;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * the class representing the main block of the file. its variables are global variables
 */
public class GlobalBlock extends Block {

    //LinkedList<String> lines =new LinkedList<String>();
   // HashMap<String, Variable> localVariables;

    /**
     * constructor
     * @param lines - the string lines that make up the block
     */
    public GlobalBlock(LinkedList<String> lines) throws sJavaException {
        super(null,lines,null,0);
    }

    @Override
    public void setVariables(LinkedList<String> variables) throws sJavaException {
        VariableFactory factory = new VariableFactory(variables,this);
        this.localVariables = factory.getVariables();
    }

    @Override
    public String getName() {
        return "global";
    }

}
