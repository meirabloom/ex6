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

    //* Constants *//
    private static final int METHOD_BRACKET_FACTOR = 1;
    private static final int NAME_PLACE = 1;
    private static final int TYPE_PLACE = 0;
    private static final int NAME_GROUP = 2;
    private static final int TYPE_GROUP = 1;


    //* Regex *//
    private static final String VARIABLE =
            "\\s*(int|double|String|boolean|char)\\s*(([^_\\s\\d]\\w*|_\\w+)\\s*)";
    private static String METHOD_SIGNATURE = "(void)\\s+([a-zA-z]\\w*)\\s*\\((.*)\\)\\s*\\{\\s*";
    private static final String NAME_PATTERN = "([^_\\s\\d]\\w*|_\\w+)\\s*";
    private static final String EMPTY_PATTERN = "\\s*";

    private String[] paramTypes;
    private String[] paramNames;
    private String methodName;
    private MethodParser methodParser;
    private HashMap<String, MethodBlock> methods;

    /**
     * constructor for method block
     * @param parent - the global block in which the method is nested
     * @param lines - method lines
     * @throws sJavaException
     */
    MethodBlock(Block parent, LinkedList<String> lines,
                HashMap<String, MethodBlock> methods)
            throws sJavaException {
        super(parent, lines,  methods,METHOD_BRACKET_FACTOR);
        extractMethodComponents();
        methodParser = new MethodParser(lines,this, this.factory);
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

    /**
     * extract list of method parameter types and method parameter names.
     * @throws sJavaException - if there is an illegal line.
     */
    private void extractMethodComponents() throws sJavaException {
        String methodSignature = lines.getFirst().trim();
        String methodParamLine = methodSignature.substring((methodSignature.indexOf("(") + 1),
                    methodSignature.indexOf(")"));
        Pattern p = Pattern.compile(METHOD_SIGNATURE);
        Matcher m = p.matcher(methodSignature);
        if(m.matches()) {
            methodName = m.group(NAME_GROUP).trim();
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
            String[] paramElements = getTypeAndName(paramArray[i]);
            paramTypes[i] = paramElements[TYPE_PLACE].trim();
            paramNames[i] = paramElements[NAME_PLACE].trim();
        }
    }



    /**
     * receives a line of "type varName" where type is the type and var is the var name and returns a list
     * where the first element is the type and the second element is the name.
     * @param line - line to parse
     * @return - string array with type in the first cell and name in the second
     */
    private String[] getTypeAndName(String line) throws sJavaException {
        String[] typeAndName = new String[2];
        Pattern varPattern = Pattern.compile(VARIABLE);
        Matcher m = varPattern.matcher(line);
        if(m.matches()){
            typeAndName[TYPE_PLACE] = m.group(TYPE_GROUP);
            typeAndName[NAME_PLACE] = m.group(NAME_GROUP);
            checkParamType(typeAndName[TYPE_PLACE]);
            checkParamName(typeAndName[NAME_PLACE]);

       }else{
            throw new sJavaException("illegal parameters");
        }
        return typeAndName;
    }

    /**
     * checks parameter name: that the structure is legal, and that the name does not exist already
     * @param name - name to check
     * @throws sJavaException - if name is illegal
     */
    private void checkParamName(String name) throws sJavaException {
        Pattern namePattern = Pattern.compile(NAME_PATTERN);
        if (!namePattern.matcher(name).matches() && !name.equals("_")){
            throw new sJavaException("illegal parameter name");
        }
        for(String param:paramNames){
            if(name.equals(param)) { throw new sJavaException("repetitive param name ");}
        }
    }

    /**
     * checks parameter type
     * @param type - paremeter type
     * @throws sJavaException - if the type is wrong
     */
    private void checkParamType(String type) throws sJavaException {
        boolean condition =
                (type.equals("int") || type.equals("double") || type.equals("boolean")
                        || type.equals("String") || type.equals("char"));
        if(!condition) {
            throw new sJavaException("wrong param type");
        }

    }

    /**
     * checks parameters in method call to make sure they are compatible with method parameters.
     * @param params - the parameters that were used to call te method, which we want to check.
     * @throws sJavaException - if call was illegal
     */
    public void checkParamsInCall(String params) throws sJavaException {
        methodParser.checkMethodCall(params, methods);
    }

    @Override
    public String[] getParamTypes(){ return paramTypes;}

    @Override
    public String[] getParamNames(){return paramNames;}

    /**
     * getter method for method name
     * @return method name
     */
    public String getMethodName(){return methodName;}


    /**
     * creturns number of parameters the method needs to get
     * @return number of parameter (0 if none)
     */
    public int getNumOfParams() {return paramNames==null? 0 : paramNames.length;}

}


