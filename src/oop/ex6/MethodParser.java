package oop.ex6;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodParser {

    private LinkedList<String> methodLines;
    private Block block;
    private Matcher m;



    private static final String METHOD_CALL = "([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*;";
    private static final String ASSIGNMENT = "=";
    private static final String METHOD_PARAMETER_EXCEPTION_MSG = "Illegal method parameter";

    MethodParser(LinkedList<String> methodLines, Block block){
       this.methodLines = methodLines;
       this.block = block;
    }

    public void checkMethod(){ //

    }

    /**
     *
     * @param parameterLine
     * @return
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



    private void validateCallParams(LinkedList<String> params){
        for(String singleParam: params){ //check all params are initialized

        }

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
        //if(!curMethod.validCallParams(allParams)){ throw new sJavaException("invalid method params");}

        //ValidateCallParams()
    }

}
