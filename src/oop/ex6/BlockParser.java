package oop.ex6;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * iterates over the string lines of code and parses them into block objects according to their type
 */
public class BlockParser {

    private LinkedList<String> innerBlockLines = new LinkedList<String>();
    private LinkedList<String> variables = new LinkedList<String>();
    private HashMap<String, MethodBlock> methods = new  HashMap<String, MethodBlock>();
    private Matcher m;

    private int blockCounter = 0;
    private Queue<Block> blocksToRead = new LinkedList<Block>();


    //constants
    private static final String METHOD_CALL_LINE = "methodCall";
    private static final String VARIABLE_ASSIGNMENT_LINE = "VarAssignment";
    private static final String RETURN_LINE = "return";
    private static final String BLOCK_END_LINE = "}";
    private static final String VARIABLE_INIT_LINE = "varInit";
    private static final String METHOD_INIT_LINE = "methodInit";
    private static final String IF_WHILE_BLOCK_LINE = "ifWhileBlock";

    private static final String ASSIGNMENT = "=";
    private static final String METHOD_PARAMETER_EXCEPTION_MSG = "Illegal method parameter";

    // Regexs
    private static final String METHOD_INIT = "(void)\\s+([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*\\{";
    private static final String METHOD_CALL = "([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*;";
    private static final String VARIABLE_INIT = "(final\\s+)?\\s*(int|double|String|boolean|char)\\s+(.*)(;)";
    private static final String VARIABLE_ASSIGNMENT = "([a-zA-z]\\w*)\\s*=(.+)\\w*;";
    private static final String CONDITION_SIGNATURE = "^\\s*(while|if)\\s*\\((.+)\\)\\s*\\{\\s*";
    private static final String RETURN = "\\s*return;\\s*";
    private static final String BLOCK_END = "\\s*}\\s*";



    /**
     *
     * @param lines
     * @param block
     * @throws sJavaException
     */
    public void readBlock(LinkedList<String> lines, Block block) throws sJavaException {

        for (String line: lines) {
            String type = parseLine(line);
            readLine(type, line, block);
        }
        for (Block newBlock : blocksToRead) { //TODO the prob is that the methods get here
            // with line as an empty array, we need to figure out why
            blocksToRead.remove();
            readBlock(newBlock.lines, newBlock);
        }
     }

    /**
     * Receives a line from a block and acts according to the line type. This Method also adds new nested
     * blocks to queue of blocks to check.
     * @param type - type of line (method call, variable initiation etc.)
     * @param line - the whole line
     * @param block - current block to read
     * @throws sJavaException - if the line does not match any of the legal lines
     */
    private void readLine(String type, String line , Block block)
            throws sJavaException {
        switch (type){
            case VARIABLE_INIT_LINE:
                if(blockCounter==0){
                    variables.add(line);
                }else{
                    innerBlockLines.add(line);
                }
                break;

            case METHOD_INIT_LINE:
                if(!block.getName().equals("global")){
                    throw new sJavaException("method declared outside main block");
                }
                blockCounter++;
                innerBlockLines.add(line);
                break;

            case IF_WHILE_BLOCK_LINE:
                if(blockCounter==0) {
                    if (block.getName().equals("global")) {
                        throw new sJavaException("If or While block not in method");
                    }
                }
                blockCounter++;
                innerBlockLines.add(line);
                break;

            case METHOD_CALL_LINE:
                if(block.getName().equals("global")){
                    throw new sJavaException("Method call in global scope");
                }
                if(blockCounter==0){
                   checkMethodCall(line, methods);
                }else{
                    innerBlockLines.add(line);
                }
                break;
            case VARIABLE_ASSIGNMENT:
                if(blockCounter==0){
                    checkVarAssignment(line,block);
                }else{
                    innerBlockLines.add(line);
                }
                break;

            case RETURN_LINE:
                innerBlockLines.add(line);
                break;

            case BLOCK_END_LINE:
                blockCounter--;
                if(blockCounter>0){
                    innerBlockLines.add(line);
                }
                if(blockCounter==0){
                    if(block.getName().equals("global")) {
                        //global scope only has nested method blocks, no if\while blocks
                        innerBlockLines.add(line);
                        MethodBlock newMethod = new MethodBlock(block, innerBlockLines, variables, methods);
                        if(methods.containsKey(newMethod.getMethodName())){
                            throw new sJavaException("method overloading");
                        }
                        blocksToRead.add(newMethod);
                        methods.put(newMethod.getMethodName(),newMethod);
                    } else {
                        blocksToRead.add(new ConditionBlock(block, innerBlockLines, variables, methods));
                    }
                    innerBlockLines.clear();
                    variables.clear();
                }
                break;

            default:
                throw new sJavaException("Illegal line");
        }
    }


    /**
     * receives a line of code and determines the type of line
     * @param line - the code line to check
     * @return - the name of the type of line
     */
    private String parseLine(String line) throws sJavaException {
        if(Pattern.compile(VARIABLE_INIT).matcher(line).matches()){
            return VARIABLE_INIT_LINE;
        }
        if(Pattern.compile(METHOD_INIT).matcher(line).matches()){
            return METHOD_INIT_LINE;
        }
        if(Pattern.compile(VARIABLE_ASSIGNMENT).matcher(line).matches()){
            return VARIABLE_ASSIGNMENT_LINE;
        }
        if(Pattern.compile(METHOD_CALL).matcher(line).matches()){
            return METHOD_CALL;
        }
        if(Pattern.compile(RETURN).matcher(line).matches()){
            return RETURN_LINE;
        }
        if(Pattern.compile(CONDITION_SIGNATURE ).matcher(line).matches()){
            return IF_WHILE_BLOCK_LINE;
        }
        if(Pattern.compile(BLOCK_END).matcher(line).matches()){
            return BLOCK_END_LINE;
        }
        else{
            throw new sJavaException("illegal line");
        }

    }


    /**
     * Checks variable assignment is legal
     * @param line - assignment line
     * @param block - current block
     * @throws sJavaException - if assignment is illegal: if variables not found, if Variable not assigned
     * or if there are incompatible types.
     */
    void checkVarAssignment(String line, Block block) throws sJavaException {
        String[] variables = line.split("=");
        for (String var : variables) {
            var.trim();
        }
        Variable var1 = block.searchForVar(variables[0]);
        Variable var2 = block.searchForVar(variables[1]);
        if(var1==null || var2==null){ throw new sJavaException("variables not found");}
        if(!var2.assigned){ throw new sJavaException("Variable not assigned");}
        if(!var2.checkAssignment(var1.varType)){ throw new sJavaException("incompatible types");}
    }

    /**
     * Checks if method call is ok: checks if the structure of the call follows expected structure, checks
     * that the cal is to an existing method, and checks the parameters are legal.
     * @param methodCallLine - method call line
     * @throws sJavaException - if there s an illegal method call, a call to un-initialized method or
     * invalid method params.
     */
    public void checkMethodCall(String methodCallLine, HashMap<String, MethodBlock> methods)
            throws sJavaException {
        Pattern methodLinePattern = Pattern.compile(METHOD_CALL);
        m = methodLinePattern.matcher(methodCallLine);
        String name;
        String params;
        if (m.matches()) {
            name = m.group(1);
            params = m.group(2);
        } else {
            throw new sJavaException("illegal method call");
        }
        if (!methods.containsKey(name)) {
            throw new sJavaException("call to un-initialized method");
        }
        MethodBlock curMethod = methods.get(name);
        curMethod.checkParamsInCall(params);
    }

}
