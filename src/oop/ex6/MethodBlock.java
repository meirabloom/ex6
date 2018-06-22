package oop.ex6;

import oop.ex6.MethodParser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a class representing the method block
 */
public class MethodBlock extends Block{

    //Constants
    private static String METHOD_SIGNATURE = "(void)\\s+([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*\\{\\s*";
    private static final String NAME_PATTERN = "([^_\\s\\d]\\w*|_\\w+)\\s*";
    private static final String EMPTY_PATTERN = "\\s*";
    private static final int METHOD_BRACKET_FACTOR = 1;
    private static final String VARIABLE =
            "\\s*(int|double|String|boolean|char)\\s*(([^_\\s\\d]\\w*|_\\w+)\\s*)";

    private String[] paramTypes;
    private String[] paramNames;
    private String methodName;
    private MethodParser methodParser;
    private HashMap<String, MethodBlock> methods;
   // private int numOfParams;

    /**
     * constructor
     * @param parent - the global block in which the method is nested
     * @param lines
     * @throws sJavaException
     */
    MethodBlock(Block parent, LinkedList<String> lines,
                HashMap<String, MethodBlock> methods)
            throws sJavaException {
        super(parent, lines,  methods,METHOD_BRACKET_FACTOR);
        extractMethodComponents();
        methodParser = new MethodParser(lines,this);
        methodParser.checkMethod();
        this.methods = methods;
        String header = super.lines.getFirst();
        String end = super.lines.getLast();
        super.lines.remove(header);
        super.lines.remove(end);
    }

    @Override
    public String getName() {
        return "method";
    }

//    /**
//     * sets the method components
//     */
//    private void extractMethodComponents() throws sJavaException {
//        String methodSignature = lines.getFirst().trim();
//        String methodParamLine = methodSignature.substring((methodSignature.indexOf("(") + 1),
//                methodSignature.indexOf(")"));
//        Pattern p = Pattern.compile(METHOD_SIGNATURE);
//        Matcher m = p.matcher(methodSignature);
//        if (m.matches()) {
//            methodName = m.group(2).trim();
//        }
//        String[] paramArray = methodParamLine.trim().split(",");
//        if (Pattern.compile(EMPTY_PATTERN).matcher(methodParamLine).matches()) {
//            numOfParams = 0;
//            return;
//        }
//        numOfParams = paramArray.length;
//        paramNames = new String[numOfParams];
//        paramTypes = new String[numOfParams];
//    }
         //sets the method components

        private void extractMethodComponents() throws sJavaException {
            String methodSignature = lines.getFirst().trim();
            String methodParamLine = methodSignature.substring((methodSignature.indexOf("(") + 1),
                    methodSignature.indexOf(")"));
            Pattern p = Pattern.compile(METHOD_SIGNATURE);
            Matcher m = p.matcher(methodSignature);
            if(m.matches()) {
                methodName = m.group(2).trim();
            }
            if (Pattern.compile(EMPTY_PATTERN).matcher(methodParamLine).matches()) {
                return;
            }
            String[] paramArray = methodParamLine.trim().split(",");
            int paramNum = paramArray.length;
            paramNames = new String[paramNum];
        paramTypes = new String[paramNum];
        for (int i = 0; i < paramArray.length; i++) {
            if(paramArray[i].equals("")){ throw new sJavaException("illegal param line");}
            paramArray[i] = paramArray[i].trim();
            String[] paramElements = getTypeAndName(paramArray[i]);//, paramNames);
            paramTypes[i] = paramElements[0].trim();
            paramNames[i] = paramElements[1].trim();
        }
    }



    /**
     * receives a line of "type varName" where type is the type and var is the var name and returns a list
     * where the first element is the type and the second element is the name.
     * @param line
     * @return
     */
    private String[] getTypeAndName(String line) throws sJavaException {
        String[] typeAndName = new String[2];
        Pattern varPattern = Pattern.compile(VARIABLE);
        Matcher m = varPattern.matcher(line);
        if(m.matches()){
            typeAndName[0] = m.group(1);
            typeAndName[1] = m.group(2);
            checkParamType(typeAndName[0]);
            checkParamName(typeAndName[1]);

       }else{
            throw new sJavaException("illegal parameters");
        }
        return typeAndName;
    }

    private void checkParamName(String name) throws sJavaException {
        Pattern namePattern = Pattern.compile(NAME_PATTERN);
        if (!namePattern.matcher(name).matches() && !name.equals("_")){
            throw new sJavaException("illegal parameter name");
        }
        for(String param:paramNames){
            if(name.equals(param)) { throw new sJavaException("repetitive param name ");}
        }
    }

    private void checkParamType(String type) throws sJavaException {
        boolean condition =
                (type.equals("int") || type.equals("double") || type.equals("boolean")
                        || type.equals("String") || type.equals("char"));
        if(!condition) {
            throw new sJavaException("wrong param type");
        }

    }

    public void checkParamsInCall(String params) throws sJavaException {
        methodParser.checkMethodCall(params, methods);
    }

    @Override
    public String[] getParamTypes(){ return paramTypes;}

    @Override
    public String[] getParamNames(){return paramNames;}

    public String getMethodName(){return methodName;}


    public int getNumOfParams() {
        return paramNames.length;
    }
}
