package oop.ex6;

import oop.ex6.MethodBlock;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.SplittableRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * iterates over the string lines of code and parses them into block objects according to their type
 */
public class BlockParser {

    private LinkedList<String> innerBlockLines = new LinkedList<String>();
    private HashMap<String, MethodBlock> methods = new  HashMap<String, MethodBlock>();
    private Matcher m;

    private int blockCounter = 0;
    private Queue<Block> blocksToRead;


    //constants
    private static final String METHOD_CALL_LINE = "methodCall";
    private static final String VARIABLE_ASSIGNMENT_LINE = "VarAssignment";
    private static final String RETURN_LINE = "return";
    private static final String BLOCK_END_LINE = "}";
    private static final String VARIABLE_INIT_LINE = "varInit";
    private static final String METHOD_INIT_LINE = "methodInit";
    private static final String IF_WHILE_BLOCK_LINE = "ifWhileBlock";

    //* Regex *//
    private static final String METHOD_INIT = "\\s*(void)\\s+([a-zA-Z]\\w*)\\s*\\((.*)\\)\\s*\\{\\s*";
    private static final String METHOD_CALL = "\\s*([a-zA-Z]\\w*)\\s*\\((.*)\\)\\s*;";
    private static final String VARIABLE_INIT =
            "\\s*(final\\s+)?\\s*(int|double|String|boolean|char)\\s*(.*)(;)\\s*";
    private static final String VARIABLE_ASSIGNMENT = "\\s*([a-zA-Z]\\w*)\\s*=(.+)\\w*;\\s*";
    private static final String CONDITION_SIGNATURE = "^\\s*(while|if)\\s*\\((.+)\\)\\s*\\{\\s*";
    private static final String RETURN = "\\s*return;\\s*";
    private static final String BLOCK_END = "\\s*}\\s*";

    Block global;
    MethodBlock curr;

    /**
     * Constructor for blockparser
     * @param global global block
     */
    public BlockParser(Block global){
        this.global = global;
        blocksToRead = new LinkedList<Block>();
    }

    /**
     * read all the blocks in the file, each read creting all the sub-blocks abd reading them as well.
     * @param lines - lines of the block
     * @param block - current block
     * @throws sJavaException
     */
    public void readBlock(LinkedList<String> lines, Block block, MethodBlock method) throws sJavaException {
        curr = method;
        for (String line: lines) {
            String type = parseLine(line);
            readLine(type, line, block);
        }
        if(blockCounter!=0) {throw new sJavaException("wrong number of brackets");}
        for (Block newBlock : blocksToRead) {
            blocksToRead.remove();
            innerBlockLines = new LinkedList<String>();
            MethodBlock methodBlock = block.getName().equals("global") ?(MethodBlock) newBlock : method;
            readBlock(newBlock.lines, newBlock, methodBlock);
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
                if(blockCounter>0){ innerBlockLines.add(line); }
                break;

            case METHOD_INIT_LINE:
                if(!block.getName().equals("global") &&
                        !(block.parent.getName().equals("global") && block.lines.getFirst().equals(line))){
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
                if(blockCounter == 0 && block.getName().equals("global")){
                    throw new sJavaException("Method call in global scope");
                }
                if(blockCounter==0) {checkMethodCall(line, methods);
                }else { innerBlockLines.add(line); }
                break;
            case VARIABLE_ASSIGNMENT_LINE:
                if(blockCounter!=0) { innerBlockLines.add(line);}
                break;

            case RETURN_LINE:
                if(blockCounter==0 && (block.getName().equals("global"))){
                    throw new sJavaException("return in global scope");
                }if(blockCounter > 0) { innerBlockLines.add(line); }
                break;

            case BLOCK_END_LINE:
                blockCounter--;
                if(blockCounter>0){
                    innerBlockLines.add(line);
                }
                if(blockCounter==0){
                    innerBlockLines.add(line);
                    if (block.getName().equals("global")) {
                            //global scope only has nested method blocks, no if\while blocks
                            MethodBlock newMethod = new MethodBlock(block, innerBlockLines,  methods);
                            if (methods.containsKey(newMethod.getMethodName())) {
                                throw new sJavaException("method overloading");
                            }
                            blocksToRead.add(newMethod);
                            methods.put(newMethod.getMethodName(), newMethod);
                        } else {
                            blocksToRead.add(new ConditionBlock(block, innerBlockLines, methods));
                        }

                        innerBlockLines = new LinkedList<>();
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
            return METHOD_CALL_LINE;
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
        curr.checkParamsInCall(methodCallLine);
    }

}
