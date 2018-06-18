package oop.ex6.main;

import oop.ex6.InitialParser;
import oop.ex6.sJavaException;

import java.io.*;

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
            BufferedReader br = new BufferedReader(new FileReader(file));
            InitialParser firstParser = new InitialParser(file); //TODO where should we close bufferedreader?


            System.out.println(LEGAL_CODE);
        }
        catch (sJavaException s){
            System.out.println(ILLEGAL_CODE);
        }
        //TODO add the rest of the options
        catch (FileNotFoundException e) {
            System.out.println(IO_ERRORS);
            System.err.println(BAD_FILE_ERROR_MSG);
        }
    }
}
