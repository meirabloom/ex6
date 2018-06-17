package oop.ex6;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a class representing the method block
 */
public class MethodBlock extends Block{

    static final String RETURN_SIGNATURE = "return;";
    static final String END_METHOD_SIGNATURE = "\\s*}\\s*";

    //exception message
    static final String MISSING_RETURN_EXCEPTION = "missing proper return statement";
    static final String MISSING_BRACKET = "Missing }";

    /**
     * constructor
     * @param parent - the global block in which the method is nested
     * @param lines
     * @param globalVariables
     * @param methods
     * @param localVariable
     * @throws sJavaException
     */
    MethodBlock(Block parent, LinkedList<String> lines, LinkedList<Variable> globalVariables,
                LinkedList<Block> methods, LinkedList<String> localVariable) throws sJavaException {
        super(parent, lines, globalVariables, methods, localVariable);
    }

    boolean checkMethodEnding() throws sJavaException {
        Pattern endPattern = Pattern.compile(END_METHOD_SIGNATURE);
        Matcher m = endPattern.matcher(lines.getLast());
        if (!lines.get(lines.size()-1).equals(RETURN_SIGNATURE)){
            throw new sJavaException(MISSING_RETURN_EXCEPTION);
        }
        if (!m.matches()) {
            throw new sJavaException(MISSING_BRACKET);
        }
    }



}
