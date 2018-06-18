package oop.ex6;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a class representing the method block
 */
public class MethodBlock extends Block{

    //Constants
    private static final String MISSING_RETURN_EXCEPTION = "missing proper return statement";
    private static final String MISSING_BRACKET = "Missing }";
    private static final String RETURN_SIGNATURE = "return;";
    private static final String END_METHOD_SIGNATURE = "\\s*}\\s*";

    String[] paramTypes;

    /**
     * constructor
     * @param parent - the global block in which the method is nested
     * @param lines
     * @param methods
     * @param localVariable
     * @throws sJavaException
     */
    MethodBlock(Block parent, LinkedList<String> lines,
                LinkedList<String> methods, LinkedList<String> localVariable) throws sJavaException {
        super(parent, lines, methods, localVariable);
        checkMethodEnding();
        extractMethodParams();
    }

    @Override
    public String getName() {
        return "method";
    }

    /**
     * @return an array of the methods parameters
     */
    private void extractMethodParams() { // TODO: finish this method (does not work atm)
        String[] paramArray = lines.getFirst().split(",");
        for (int i =0; i < paramArray.length; i++) {
            paramArray[i] = paramArray[i].trim();
            String[] paramElements = paramArray[i].split(" ");
            paramArray[i] = paramArray[i].trim();
        }
        paramTypes =  paramArray;
    }

    /**
     * checks if the method ends with a return statement and curly brackets
     * @throws sJavaException - an exception thrown if the method ends illegally
     */
    private void checkMethodEnding() throws sJavaException {
        Pattern endPattern = Pattern.compile(END_METHOD_SIGNATURE);
        Matcher m = endPattern.matcher(lines.getLast());
        if (!lines.get(lines.size()-1).equals(RETURN_SIGNATURE)){
            throw new sJavaException(MISSING_RETURN_EXCEPTION);
        }
        if (!m.matches()) {
            throw new sJavaException(MISSING_BRACKET);
        }
    }

    public String[] getParamTypes(){ return paramTypes;}




}
