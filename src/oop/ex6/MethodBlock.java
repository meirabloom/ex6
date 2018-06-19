package oop.ex6;

import java.util.HashMap;
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
    private static String METHOD_SIGNATURE = "(void)\\s+([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*\\{\\s*";

    private String[] paramTypes;
    private String[] paramNames;
    private String methodName;
    private MethodParser methodParser;

    /**
     * constructor
     * @param parent - the global block in which the method is nested
     * @param lines
     * @param localVariable
     * @throws sJavaException
     */
    MethodBlock(Block parent, LinkedList<String> lines, LinkedList<String> localVariable,
                HashMap<String, MethodBlock> methods)
            throws sJavaException {
        super(parent, lines, localVariable, methods);
        checkMethodEnding();
        extractMethodComponents();
        methodParser = new MethodParser(lines,this);
        methodParser.checkMethod();
    }

    @Override
    public String getName() {
        return "method";
    }

    /**
     * sets the method components
     */
    private void extractMethodComponents() {
        String methodSignature = lines.getFirst().trim();
        String methodParamLine = methodSignature.substring((methodSignature.indexOf("(") + 1),
                methodSignature.indexOf(")"));
        String[] paramArray = methodParamLine.trim().split(",");
        int paramNum = paramArray.length;
        paramNames = new String[paramNum];
        paramTypes = new String[paramNum];
        for (int i = 0; i < paramArray.length; i++) {
            paramArray[i] = paramArray[i].trim();
            String[] paramElements = paramArray[i].split(" ");
            paramTypes[i] = paramElements[0].trim();
            paramNames[i] = paramElements[1].trim();
        }
        Pattern p = Pattern.compile(METHOD_SIGNATURE);
        Matcher m = p.matcher(methodSignature);
        if(m.matches()) {
            methodName = m.group(2).trim();
        }
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

    public void checkParams(String params) throws sJavaException {
        methodParser.checkMethodCall(params, methods);
    }

    public String[] getParamTypes(){ return paramTypes;}

    @Override
    public String[] getParamNames(){return paramNames;}

    public String getMethodName(){return methodName;}





}
