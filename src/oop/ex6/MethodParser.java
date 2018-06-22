package oop.ex6;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodParser {

    private LinkedList<String> methodLines;
    private Block block;
    private Matcher m;
    private VariableFactory factory;


    private static final String MISSING_RETURN_EXCEPTION = "missing proper return statement";
    private static final String MISSING_BRACKET = "Missing }";
    private static final String RETURN_SIGNATURE = "\\s*return;\\s*";
    private static final String METHOD_CALL = "\\s*([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*;";
    private static final String ASSIGNMENT = "=";
    private static final String METHOD_PARAMETER_EXCEPTION_MSG = "Illegal method parameter";
    private static final String END_METHOD_SIGNATURE = "\\s*}\\s*";


    MethodParser(LinkedList<String> methodLines, Block block, VariableFactory factory){
       this.methodLines = methodLines;
       this.block = block;
       this.factory = factory;
    }

    public void checkMethod() throws sJavaException {
        checkMethodEnding(methodLines.getLast(),methodLines.get(methodLines.size()-2));
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
        if(m.matches()){
            name = m.group(1);
            params = m.group(2);
        }else{ throw new sJavaException("illegal method call"); }
        if(!methods.containsKey(name)){ throw new sJavaException("call to un-initialized method");}
        MethodBlock curMethod = methods.get(name);
        LinkedList<String> allParams = parseParameters(params);
        if(allParams.size()!= curMethod.getNumOfParams()){
            throw new sJavaException("wrong num of params");
        }
        validateCallParams(allParams, curMethod);
    }

    /**
     * Receives string of all the potential parameters in a method call, and separates them to individual
     * parameters. Checks the structure of the individual parameters is legal: they do not contain an
     * assignment in them (can't have foo(b = 5).
     * @param parameterLine - line of params
     * @return - the method parameters - linkedlist of all the params
     * @throws sJavaException - if there is an assignment
     */
    private LinkedList<String> parseParameters(String parameterLine) throws sJavaException {
        LinkedList<String> params = new LinkedList<String>();
        String[] paramArray = parameterLine.split(",");
        if (paramArray.length == 1 && paramArray[0].equals("")){
            return params;
        }
        for (String singleParam : paramArray) {
            if (singleParam.contains(ASSIGNMENT)) {
                throw new sJavaException(METHOD_PARAMETER_EXCEPTION_MSG);
            }
            params.add(singleParam.trim());
        }
        return params;
    }


    /**
     * validates the parameters of the method call are legal: that they are all initialized veriables in the
     * correct scope, that their type match the methods parameter types.
     * @param params - parameters to check
     * @param method -
     * @throws sJavaException
     */
    private void validateCallParams(LinkedList<String> params, MethodBlock method) throws sJavaException {
        String[] types = method.getParamTypes();

        for(int i=0; i< params.size(); i++){
            factory.checkValue(types[i],params.get(i));
        }
    }

    /**
     * checks if the method ends with a return statement and curly brackets
     * @throws sJavaException - an exception thrown if the method ends illegally
     */
    private void checkMethodEnding(String endStatement, String returnStatement ) throws sJavaException {
        if(!Pattern.compile(RETURN_SIGNATURE).matcher(returnStatement).matches()){
               throw new sJavaException(MISSING_RETURN_EXCEPTION);
        }
        if (!Pattern.compile(END_METHOD_SIGNATURE).matcher(endStatement).matches()) {
            throw new sJavaException(MISSING_BRACKET);
        }
    }






}
