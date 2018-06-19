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
    private static final String METHOD_CALL = "([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*;";
    private static final String ASSIGNMENT = "=";
    private static final String METHOD_PARAMETER_EXCEPTION_MSG = "Illegal method parameter";

    // Regexs
    static final String METHOD_SIGNATURE = "(void)\\s+([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*{";
  //  static final String METHOD_CALL = "([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*;";
    static final String VARIABLE_DECLERATION = "(final\\s+)?\\s*(int|double|String|boolean|char)\\s+(.*)(;)";
    static final String VARIABLE_ASSIGNMENT = "([a-zA-z]\\w*)\\s*=(.+)\\w*;";
    static final String CONDITION_SIGNATURE = "^\\s*(while|if)\\s*\\((.+)\\)\\s*\\{\\s*";



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
                   // checkMethodCall(line);
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
                        MethodBlock newMethod = new MethodBlock(block, innerBlockLines, variables);
                        if(methods.containsKey(newMethod.getMethodName())){
                            throw new sJavaException("method overloading");
                        }
                        blocksToRead.add(newMethod);
                        methods.put(newMethod.getMethodName(),newMethod);
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

    /**
     *
     * @param lines
     * @param block
     * @throws sJavaException
     */
    void readBlock(LinkedList<String> lines, Block block) throws sJavaException {
        for (String line: lines) {
            String type = parseLine(line);
            readLine(type, line, block);
            for (Block newBlock : blocksToRead) {
                blocksToRead.remove();
                readBlock(innerBlockLines, newBlock);
            }
        }
    }

    /**
     * receives a line of code and determines the type of object
     * @param line - the code line to check
     * @return - the name of the type of line
     */
    private String parseLine(String line) {


    }


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

}
