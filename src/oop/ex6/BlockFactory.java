package oop.ex6;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * iterates over the string lines of code and parses them into block objects according to their type
 */
public class BlockFactory {

    /** the array containing the blocks */
    ArrayList<Block> blockList = new ArrayList<>();

    //block types
    static final String METHOD = "method";
    static final String WHILE = "while";
    static final String IF = "if";

    //regex
    static final String METHOD_SIGNATURE = "(void)\\s+([a-zA-z]\\w*)\\s*+\\((.)\\)\\s{";


    /**
     * iterates over the list of lines and creates the block objects, adding each block to the list of blocks
     */
    public void parseBlocks() { //TODO


    }

    /**
     *
     * @param type - the type of the block: method, if or while
     * @param blockLines - the lines making up the block //TODO not sure if this is what we want to do
     * @param parentBlock - the scope in which this block is nested
     * @return - the new block object according to its type
     */
    public Block createBlock(String type, LinkedList blockLines , Block parentBlock){ //TODO exceptions
        switch (type){
            case METHOD:
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
