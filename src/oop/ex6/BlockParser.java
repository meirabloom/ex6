package oop.ex6;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * iterates over the string lines of code and parses them into block objects according to their type
 */
public class BlockParser {

    /** the array containing the blocks */
    ArrayList<Block> blockList = new ArrayList<>();

    //constants
    static final String METHOD = "method";
    static final String WHILE = "while";
    static final String IF = "if";
    static final String ASSIGNMENT = "=";
    static final String METHOD_PARAMETER_EXCEPTION_MSG = "Illegal method parameter";

    // Regexs
    static final String METHOD_SIGNATURE = "(void)\\s+([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*{";
    static final String METHOD_CALL = "([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*;";
    static final String VARIABLE_DECLERATION = "(final\\s+)?\\s*(int|double|String|boolean|char)\\s+(.*)(;)";
    static final String VARIABLE_ASSIGNMENT = "([a-zA-z]\\w*)\\s*=(.+)\\w*;";


    private String[] parseMethodParams(String parameterLine) throws sJavaException {
        String[] paramArray = parameterLine.split(",");
        for (int i =0; i < paramArray.length; i++) {
            paramArray[i] = paramArray[i].trim() + ";";
            if (paramArray[i].contains(ASSIGNMENT)) {
                throw new sJavaException(METHOD_PARAMETER_EXCEPTION_MSG);
            }
        }
        return paramArray;
    }
    /**
     *
     * @param type - the type of the block: method, if or while
     * @param blockLines - the lines making up the block //TODO not sure if this is what we want to do
     * @param parentBlock - the scope in which this block is nested
     * @return - the new block object according to its type
     */
    public Block createBlock(String type, String line, LinkedList blockLines , Block parentBlock)
            throws sJavaException { //TODO
        switch (type){
            case METHOD:
                String[] params = parseMethodParams(line);

                // Parmaters = group(3)
                // Hash p = VariableFactory(parameters)
                return null;//new MethodBlock(); //TODO what paramaters do these objects receive?
            case WHILE:
                return  null;//  new WhileBlock();
            case IF:
                return null;// new IfBlock();
            default:
                return null;
        }
    }


}
