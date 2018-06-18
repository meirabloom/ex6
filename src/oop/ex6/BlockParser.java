package oop.ex6;

import sun.awt.image.ImageWatched;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
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
    private LinkedList<MethodBlock> methods = new LinkedList<MethodBlock>();
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
    static final String METHOD_SIGNATURE = "(void)\\s+([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*{";
    static final String METHOD_CALL = "([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*;";
    static final String VARIABLE_DECLERATION = "(final\\s+)?\\s*(int|double|String|boolean|char)\\s+(.*)(;)";
    static final String VARIABLE_ASSIGNMENT = "([a-zA-z]\\w*)\\s*=(.+)\\w*;";


    /**
     * Receives a line from a block and acts according to the line type. This Method also adds new nested
     * blocks to queue of blocks to check.
     * @param type - type of line (method call, variable initiation etc.)
     * @param line - the whole line
     * @param block - current block to read
     * @throws sJavaException - if the line does not match any of the legal lines
     */
    public void readBlock(String type, String line , Block block)
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
                if(block.getName().equals("global")){
                    throw new sJavaException("If or While block not in method");
                }
                blockCounter++;
                innerBlockLines.add(line);
                break;

            case METHOD_CALL_LINE:
                if(block.getName().equals("global")){
                    throw new sJavaException("Method call in global scope");
                }
                if(blockCounter==0){
                    // TODO checkMethodCall();

                }else{
                    innerBlockLines.add(line);
                }
                break;
            case VARIABLE_ASSIGNMENT:
                if(blockCounter==0){
                    // TODO: CheckAssignment();
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
                        MethodBlock newMethod = new MethodBlock(block, innerBlockLines, variables);

                        blocksToRead.add(newMethod);
                        methods.add(newMethod);
                    } else {
                        blocksToRead.add(new ConditionBlock(block, innerBlockLines, variables));
                    }
                    innerBlockLines.clear();
                    variables.clear();
                }
                break;

            default:
                throw new sJavaException("Illegal line");
        }
    }

    private void checkMethodCall(String methodCallLine) throws sJavaException {
        Pattern methodLinePattern = Pattern.compile(METHOD_CALL);
        m = methodLinePattern.matcher(methodCallLine);
        String methodName;
        String params;
        if(m.matches()){
            methodName = m.group(1);
            params = m.group(2);
        }else{
            throw new sJavaException("illegal method call");
        }
        for(MethodBlock method: methods){
            if(methods.contains(methodName)){
                throw new sJavaException("call to undefined method");
            }
        }

        String[] allParams = parseMethodCallParams(params); // will return a list of all parameters
        for(int i=0; i<allParams.length; i++){
            // checkParam( ,  : will check if it is initialized and if it is the right type
        }



    }


    /**
     * Receive a line representing a method call line parameters and returns a list of parameters.
     * @param parameterLine - line of prameters
     * @return paramArray - list of seperated parameters
     * @throws sJavaException - if the prameters are assigned
     */
    private String[] parseMethodCallParams(String parameterLine) throws sJavaException {
        String[] paramArray = parameterLine.split(",");
        for (int i =0; i < paramArray.length; i++) {
            paramArray[i] = paramArray[i].trim() + ";";
            if (paramArray[i].contains(ASSIGNMENT)) {
                throw new sJavaException(METHOD_PARAMETER_EXCEPTION_MSG);
            }
        }
        return paramArray;
    }
}
