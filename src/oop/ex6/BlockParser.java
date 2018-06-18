package oop.ex6;

import sun.awt.image.ImageWatched;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * iterates over the string lines of code and parses them into block objects according to their type
 */
public class BlockParser {

    private LinkedList<String> innerBlockLines = new LinkedList<String>();
    private LinkedList<String> variables = new LinkedList<String>();
    private LinkedList<String> methods = new LinkedList<String>();

    private int blockCounter = 0;
    private Queue<Block> blocksToRead;

    //constants
    static final String METHOD_CALL_LINE = "methodCall";
    static final String VARIABLE_ASSIGNMENT_LINE = "VarAssignment";
    static final String RETURN_LINE = "return";
    static final String BLOCK_END_LINE = "}";
    static final String VARIABLE_INIT_LINE = "varInit";
    static final String METHOD_INIT_LINE = "methodInit";
    static final String IF_WHILE_BLOCK_LINE = "ifWhileBlock";

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
                methods.add(line);
                break;

            case IF_WHILE_BLOCK_LINE:
                if(block.getName().equals("global")){
                    throw new sJavaException("If or While block not in method");
                }
                blockCounter++;
                innerBlockLines.add(line);
                break;

            case METHOD_CALL_LINE:
                if(blockCounter==0){
                    // TODO: CheckMethodCall();
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
                        blocksToRead.add(new MethodBlock(block, innerBlockLines, methods, variables));
                    } else {
                        blocksToRead.add(new ConditionBlock(block, innerBlockLines, methods, variables));
                        //TODO: who needs to know global var/ methods??
                    }
                    innerBlockLines.clear();
                    variables.clear();
                    methods.clear();
                }
                break;

            default:
                throw new sJavaException("Illegal line");
        }
    }




}
