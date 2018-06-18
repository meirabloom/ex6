package oop.ex6;

import java.util.LinkedList;

public class ParseLine {

    String line;
    LinkedList<Block> globalMethods;

    //Constance
    static final String METHOD_SIGNATURE = "(void)\\s+([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*{";
    static final String METHOD_CALL = "([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*;";
    static final String VARIABLE_DECLERATION = "(final\\s+)?\\s*(int|double|String|boolean|char)\\s+(.*)(;)";
    static final String VARIABLE_ASSIGNMENT = "([a-zA-z]\\w*)\\s*=(.+)\\w*;";


    // Constructor
    ParseLine(String line, LinkedList<Block> globalMethods){
        this.line = line;
        this.globalMethods = globalMethods;
    }


}
