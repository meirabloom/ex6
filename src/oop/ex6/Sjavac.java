package oop.ex6;

import oop.ex6.BlockParser;
import oop.ex6.GlobalBlock;
import oop.ex6.InitialParser;
import oop.ex6.sJavaException;

import java.io.*;
import java.util.LinkedList;

/**
 * the main class that runs the verifier
 */
public class Sjavac {

    /** output */
    private static final int LEGAL_CODE = 0;
    private static final int ILLEGAL_CODE = 1;
    private static final int IO_ERRORS = 2;
    private static final String BAD_FILE_ERROR_MSG = "File not found";

    public static void main(String[] args){
        try {
            File file = new File(args[0]);
            InitialParser firstParser = new InitialParser(file);
            LinkedList<String> allLines = firstParser.getLines();
            GlobalBlock global = new GlobalBlock(allLines);
            BlockParser bp = new BlockParser(global);
            bp.readBlock(allLines,global);
            System.out.println(LEGAL_CODE);
        }
       catch (IOException e) {
            System.out.println(IO_ERRORS);
            System.err.println(BAD_FILE_ERROR_MSG);
        }
        catch (sJavaException e){
            System.out.println(ILLEGAL_CODE);
            System.err.println(e.getLocalizedMessage());
        }
    }
}
