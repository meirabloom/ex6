package oop.ex6;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodParser {

    private LinkedList<String> methodLines;
    private Block block;
    private Matcher m;


    private static final String MISSING_RETURN_EXCEPTION = "missing proper return statement";
    private static final String MISSING_BRACKET = "Missing }";
    private static final String RETURN_SIGNATURE = "return;";
    private static final String METHOD_CALL = "([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*;";
    private static final String ASSIGNMENT = "=";
    private static final String METHOD_PARAMETER_EXCEPTION_MSG = "Illegal method parameter";
    private static final String END_METHOD_SIGNATURE = "\\s*}\\s*";


    MethodParser(LinkedList<String> methodLines, Block block){
       this.methodLines = methodLines;
       this.block = block;
    }

    public void checkMethod() throws sJavaException {
        checkMethodEnding(methodLines.getLast(),methodLines.get(methodLines.size()-1));
        String callLine = methodLines.getFirst();
        String paramLine = callLine.substring(callLine.indexOf("(")+1,callLine.indexOf(")"));


    }


    /**
     * Checks if method call is ok: checks if the structure of the call follows expected structure, checks
     * that the cal is to an existing method, and checks the parameters are legal.
     * @param methodCallLine - method call line
     * @throws sJavaException - if there s an illegal method call, a call to un-initialized method or
     * invalid method params.
     */
    public void checkMethodCall(String methodCallLine, HashMap<String, MethodBlock> methods) throws sJavaException {
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
        if(allParams.size()!= curMethod.getParamTypes().length){
            throw new sJavaException("wrong num of params");
        }
        validateCallParams(allParams, curMethod);
    }

    /**
     *
     * @param parameterLine - line of params
     * @return - the method parameters
     * @throws sJavaException
     */
    private LinkedList<String> parseParameters(String parameterLine) throws sJavaException {
        LinkedList<String> params = new LinkedList<String>();
        String[]  paramArray = parameterLine.split(",");
        for (String singleParam : paramArray) {
            if (singleParam.contains(ASSIGNMENT)) {
                throw new sJavaException(METHOD_PARAMETER_EXCEPTION_MSG);
            }
            params.add(singleParam.trim());
        }
        return params;
    }


    /**
     * validates the parameters of the mathod call are legal: that they are all initialized veriables in the
     * correct scope, that their type match the methods parameter types.
     * @param params - parameters to check
     * @param method -
     * @throws sJavaException
     */
    private void validateCallParams(LinkedList<String> params, MethodBlock method) throws sJavaException {
        String[] types = method.getParamTypes();
        for(int i=0; i< params.size(); i++){
            Variable curVar = block.searchForVar(params.get(i));
            if(curVar==null){
                throw new sJavaException("param not found");
            }else{
                if(!curVar.assigned){
                    throw new sJavaException("param not assigned");
                }
                if (!curVar.varType.equals(types[i])){
                    throw new sJavaException("wrong param type");
                }
            }
        }
    }

    /**
     * checks if the method ends with a return statement and curly brackets
     * @throws sJavaException - an exception thrown if the method ends illegally
     */
    private void checkMethodEnding(String returnStatement, String endStatement) throws sJavaException {
        Pattern endPattern = Pattern.compile(END_METHOD_SIGNATURE);
        Matcher m = endPattern.matcher(endStatement);
        if (!returnStatement.equals(RETURN_SIGNATURE)){
            throw new sJavaException(MISSING_RETURN_EXCEPTION);
        }
        if (!m.matches()) {
            throw new sJavaException(MISSING_BRACKET);
        }
    }




}
