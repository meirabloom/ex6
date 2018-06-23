package oop.ex6;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class Block {

    //* Constants *//
    private static final int START = 0;
    private static final int PARAM_GROUP = 3;

    //* regex *//
    private static final String VARIABLE_INIT =
            "(final\\s+)?\\s*(int|double|String|boolean|char)\\s+(.*)(;)\\s*";
    private static final String VARIABLE_ASSIGNMENT = "(\\w+)\\s*(=)\\s*(.*);\\s*";
    private static final String METHOD_INIT =
            "\\s*(void)\\s+([a-zA-Z]\\w*)\\s*\\" +
                    "((\\s*(final\\s+)?\\s*(int|double|String|boolean|char)\\s+([\\w|_]*))\\)\\s*\\{\\s*";

    Block parent;
    HashMap<String, Variable> localVariables;
    LinkedList<String> lines;
    HashMap<String, MethodBlock> methods;
    VariableFactory factory;




    /**
     * block constructor
     * @param parent parent block
     * @param lines block lines
     * @param methods block methods
     * @throws sJavaException
     */
    public Block(Block parent, LinkedList<String> lines,
                 HashMap<String, MethodBlock> methods, int factor) throws sJavaException {
        this.parent = parent;
        this.lines = lines;
        LinkedList<String> varToSet = extractVariables(factor);
        this.factory = new VariableFactory(varToSet, this);
        this.localVariables = factory.getVariables();
        checkVarAssignment(factor);
        this.methods = methods;

    }

    /**
     * read block line, find variable initialization, create variables and set them as block's
     * "local variables".
     * @param factor - balance factor
     * @return variables
     * @throws sJavaException for illegal variables
     */
    private LinkedList<String> extractVariables(int factor) throws sJavaException {
        int bracketCounter = 0;
        LinkedList<String> variables = new LinkedList<String>();
        for (String line : lines) {
            if (line.contains("{")) {
                bracketCounter++;
            }
            if (line.contains("}")) {
                bracketCounter--;
            }
            if (Pattern.compile(VARIABLE_INIT).matcher(line).matches()) {
                if (bracketCounter == factor) { variables.add(line);}
            }
            if (Pattern.matches(METHOD_INIT,line) && bracketCounter == factor) {
                Matcher m = Pattern.compile(METHOD_INIT).matcher(line);
                if (m.matches()){
                    String param = m.group(PARAM_GROUP);
                    String[] allParams = param.trim().split(",");
                    if (!(allParams.length == 1 && allParams[0].equals(""))) {
                        for (String string : allParams) {
                            string = string.trim();
                            String type = string.substring(START, string.indexOf(' '));
                            switch (type) {
                                case "int":
                                case "double":
                                case "boolean":
                                    string +="=0;";
                                    break;
                                case "String":
                                    string += "= \"\";";
                                    break;
                                case "char":
                                    string += "= \'a\';";
                                    break;
                                default:
                                    throw new sJavaException("wrong parameter type");
                            }
                            variables.add(string);
                        }
                    }
                }
            }
        }
        return variables;
    }

    /**
     * check variable assignment is legal: that the value of the assignment is of the right type of a
     * compatible pre-existing variable.
     * @param factor - balance factor
     * @throws sJavaException
     */
    private void checkVarAssignment(int factor) throws sJavaException {
        int bracketCounter = 0;
        for (String line : lines) {
            if (line.contains("{")) {
                bracketCounter++;
            }
            if (line.contains("}")) {
                bracketCounter--;
            }
            if(Pattern.compile(VARIABLE_ASSIGNMENT).matcher(line).matches()) {
                if(bracketCounter == factor) {
                    String[] variables = line.split("=");
                    String firstVar = variables[0].trim();
                    String assignmentVal = variables[1].substring(0, variables[1].indexOf(";")).trim();
                    Variable var1 = this.searchForVar(firstVar);
                    if(var1 == null) { return;}
                    if(var1.isFinal) { throw new sJavaException("changing final variable");}
                    if(!factory.checkValue(var1.varType,assignmentVal)){
                        throw new sJavaException("wrong type assigned to variable");
                    }
                    if(this.getName().equals("global")) {var1.setValue(assignmentVal);}
                }
            }
        }
    }

    /**
     * Search for variable by name recursively in this block and parent blocks.
     * @param varName - name of var to find
     * @return - variable object if found, null otherwise.
     */
    Variable searchForVar(String varName){
        Variable searchVar = null;
        Block curBlock = this;
        while(curBlock!=null){
            if(curBlock.localVariables!=null) {
                if (curBlock.localVariables.containsKey(varName)) {
                    searchVar = curBlock.localVariables.get(varName);
                }
            }
            curBlock = curBlock.parent;
        }
        return searchVar;
    }

    /**
     * get block name
     * @return block name
     */
    public abstract String getName();

    /**
     * get list of parameter names
     * @return - list of parameter names
     */
    public String[] getParamNames(){return null;}

    /**
     * get list of parameter types
     * @return list of parameter types
     */
    public String[] getParamTypes(){return null;}


    /**
     * set variables
     * @param variables variables to set
     * @throws sJavaException
     */
    public void setVariables(LinkedList<String> variables) throws sJavaException {}

    }
