package oop.ex6;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;


public abstract class Block {

    private static final String VARIABLE_INIT = "(final\\s+)?\\s*(int|double|String|boolean|char)\\s+(.*)(;)";

    Block parent;
    HashMap<String, Variable> localVariables;
    LinkedList<String> lines;
    LinkedList<Variable> globalVariables;
    HashMap<String, MethodBlock> methods;


    //constructors

    /**
     * default constructor
     */
    Block() {
    }

//    /**
//     * a constructor of a parentless block (the global block)
//     */
//    public Block(LinkedList<String> lines, int factor) {
//        this.parent = null;
//        this.lines = lines;
//        this.methods = new HashMap<String, MethodBlock>();
//        this.globalVariables = new LinkedList<Variable>();
//    }

    /**
     *
     * @param parent
     * @param lines
     * @param localVariable
     * @param methods
     * @throws sJavaException
     */
    public Block(Block parent, LinkedList<String> lines,
                 HashMap<String, MethodBlock> methods, int factor) throws sJavaException {
        this.parent = parent;
        this.lines = lines;
        LinkedList<String> varToSet = extractVariables(factor);
        VariableFactory factory = new VariableFactory(varToSet, this);
        this.localVariables = factory.getVariables();
        this.methods = methods;

    }

    public LinkedList<String> extractVariables(int factor) {
        int bracketCounter = 0;
        LinkedList<String> variables = new LinkedList<String>();
        Pattern variablePattern = Pattern.compile(VARIABLE_INIT);
        for (String line : lines) {
            if (line.contains("{")) {
                bracketCounter++;
            }
            if (line.contains("}")) {
                bracketCounter--;
            }
            if (variablePattern.matcher(line).matches()) {
                if (bracketCounter == factor) {
                    variables.add(line);
                }
            }
        }
        return variables;
    }


    /**
     *
     * @param varName
     * @return
     */
    Variable searchForVar(String varName){
        Variable searchVar = null;
        Block curBlock = this;
        while(curBlock.parent!=null){
            if(curBlock.localVariables!=null) {
                if (curBlock.localVariables.containsKey(varName)) {
                    searchVar = curBlock.localVariables.get(varName);
                }
            }
            curBlock = curBlock.parent;
        }
        return searchVar;
    }

    public abstract String getName();

    public String[] getParamNames(){return null;}

    public void setVariables(LinkedList<String> variables) throws sJavaException {}


    }
