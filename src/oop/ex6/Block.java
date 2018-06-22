package oop.ex6;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class Block {

    private static final String VARIABLE_INIT =
            "(final\\s+)?\\s*(int|double|String|boolean|char)\\s+(.*)(;)\\s*";
    private static final String VARIABLE_ASSIGNMENT = "(\\w+)\\s*(=)\\s*(.*);\\s*";
    private static final String METHOD_INIT = "\\s*(void)\\s+([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*\\{\\s*";

    Block parent;
    HashMap<String, Variable> localVariables;
    LinkedList<String> lines;
    HashMap<String, MethodBlock> methods;
    VariableFactory factory;


    //constructors


    /**
     *
     * @param parent
     * @param lines
     * @param methods
     * @throws sJavaException
     */
    public Block(Block parent, LinkedList<String> lines,
                 HashMap<String, MethodBlock> methods, int factor) throws sJavaException {

        this.parent = parent;
        this.lines = lines;
        LinkedList<String> varToSet = extractVariables(factor);
        this.factory = new VariableFactory(varToSet, this);
     //   HashMap<String,Variable> hm = factory.getVariables();
        this.localVariables = factory.getVariables();
        checkVarAssignment(factor);
        this.methods = methods;

    }

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
                if (bracketCounter == factor) {
                    variables.add(line);
                }
            }
            if (Pattern.matches(METHOD_INIT,line) && bracketCounter == factor) {
                Matcher m = Pattern.compile(METHOD_INIT).matcher(line);
                if (m.matches()){
                    String param = m.group(3);
                    String[] allParams = param.trim().split(",");
                    if (!(allParams.length == 1 && allParams[0].equals(""))) {
                        for (String string : allParams) {
                            string = string.trim();
                            String type = string.substring(0, string.indexOf(' '));
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
                    String assignmentVar = variables[1].substring(0, variables[1].indexOf(";")).trim();
                    Variable var1 = this.searchForVar(firstVar);
                    if(var1 == null) { return;}
                    if(!factory.checkValue(var1.varType,assignmentVar)){
                        throw new sJavaException("wrong type assigned to variable");}
                }
            }
        }
    }

    /**
     *
     * @param varName
     * @return
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

    public abstract String getName();

    public String[] getParamNames(){return null;}
    public String[] getParamTypes(){return null;}


    public void setVariables(LinkedList<String> variables) throws sJavaException {}


    }
