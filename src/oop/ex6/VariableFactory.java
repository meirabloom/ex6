package oop.ex6;

import oop.ex6.Block;
import oop.ex6.Variable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a class that receives a list of strings, each line representing a variable, and creates Variable objects
 * occording to the strings specifications
 */
public class VariableFactory {

    private static final String LEGAL_PATTERN = "(final\\s+)?\\s*(int|double|String|boolean|char)\\s+(.*)" +
            "(;)\\s*";
    private static final String DOUBLE_PATTERN = "-?\\d+\\.?\\d*";
    private static final String INT_PATTERN = "-?\\d+";
    private static final String STR_PATTERN = "\"[a-zA-Z]*\"";
    private static final String CHAR_PATTERN = "\'\\s*.\\s*\'";
    private static final String NAME_PATTERN = "([^_\\s\\d]\\w*|_\\w+)\\s*";
    private static final String NEW_VAL_PATTERN = "(-?\\d+\\.?\\d*)|(\"[a-zA-Z]+\")";
    private static final String ASSIGNED_PATTERN = "\\s*(\\w+)\\s*(=)\\s*(.*)";
    private static final boolean UNASSIGNED = false;
    private static final boolean ASSIGNED = true;
    private static final int NAME_PLACE = 1;
    private static final int TYPE_PLACE = 2;
    private static final int REST_OF_ELEMENTS_PLACE = 3;
    private static final int VALUE_PLACE = 3;


    private LinkedList<String> strVars;
    private HashMap<String, Variable> variables;
    private Block block;
    private Matcher m;

    /**
     * Constructor for variable factory
     * @param strVars - list of string to turn into variables
     * @param block - current block which contains the variables
     */
    VariableFactory (LinkedList<String> strVars, Block block){
        this.strVars = strVars;
        this.variables = new HashMap<String,Variable>();
        this.block = block;
    }


    /**
     * Returns a map of all variables, if they are legal.
     * @return hashmap of variables with var name as key and variable object as value.
     * @throws sJavaException if variables are not legal.
     */
    public HashMap<String, Variable> getVariables() throws sJavaException {
        for (String var : strVars) {
            Pattern general = Pattern.compile(LEGAL_PATTERN);
            m = general.matcher(var);
            if (!m.matches()) {
                throw new sJavaException("illegal variable deceleration format");
            }
            boolean isFinal = isFinal(var);
            String type = m.group(TYPE_PLACE).trim();
            String[] allVarsInLine = m.group(REST_OF_ELEMENTS_PLACE).split(",");
            for (String oneVar : allVarsInLine) {
                Pattern assignedPattern = Pattern.compile(ASSIGNED_PATTERN);
                Matcher newMatcher  = assignedPattern.matcher(oneVar);
                if (newMatcher.matches()) { // oneVar is assigned
                    String name = newMatcher.group(NAME_PLACE).trim();
                    if (!checkName(name)) {
                        throw new sJavaException("illegal name");
                    }
                    if (!checkValue(type, newMatcher.group(VALUE_PLACE).trim())) {
                        throw new sJavaException("incompatible value");
                    }
                    Variable newVar = new Variable(type, name, ASSIGNED, isFinal,
                            newMatcher.group(VALUE_PLACE));
                    variables.put(name, newVar);

                }else { // oneVar is not assigned
                    if (isFinal) {
                        throw new sJavaException("Uninitialized final val");
                    }
                    if (!Pattern.compile(NAME_PATTERN).matcher(oneVar.trim()).matches()) {
                        throw new sJavaException("illegal variables line");
                    }
                    if (!checkName(oneVar.trim())) {
                        throw new sJavaException("illegal variable name");
                    }
                    Variable newVar = new Variable(type, oneVar.trim(), UNASSIGNED, isFinal, null);
                    variables.put(oneVar.trim(), newVar);
                }
            }
        }
        return variables;
    }

    /**
     * @param var - the variable to check
     * @return - true if the variable is a final variable
     */
    private boolean isFinal(String var){ return var.startsWith("final "); }


    /**
     * Checks variables name - if it exists nd if it is the right format
     * @param name - variable name
     * @return true if name is legal, false otherwise.
     */
    private boolean checkName(String name) {
        Pattern namePattern = Pattern.compile(NAME_PATTERN);
        return namePattern.matcher(name).matches() && !variables.containsKey(name);
    }

    /**
     * Checks that the value of the new variable is compatible to the type.
     * @param type - type of the variable
     * @param value - value of the variable
     * @return true if the type is compatible, false otherwise.
     * @throws sJavaException - if the type is unrecognized
     */
    public boolean checkValue(String type, String value) throws sJavaException{
        Pattern doublePattern = Pattern.compile(DOUBLE_PATTERN);
        Pattern intPattern = Pattern.compile(INT_PATTERN);
        Pattern strPattern = Pattern.compile(STR_PATTERN);
        Pattern charPattern = Pattern.compile(CHAR_PATTERN);
        Pattern assignToNewValPattern = Pattern.compile(NEW_VAL_PATTERN);

        if(!assignToNewValPattern.matcher(value).matches()){ // assignment to existing variable
            if(variables.containsKey(value)){ // assignment to variable in the same scope
                if(!variables.get(value).assigned) {
                    throw new sJavaException("assignment to uninitialized variable");
                }
                return checkTypeAssignment(type, variables.get(value).varType);
            }
            Variable var = block.searchForVar(value);
            if(var!=null){ // assignment to variable from outer scope
                if(!var.assigned) {
                    throw new sJavaException("assignment to uninitialized variable");
                }
                return checkTypeAssignment(type,var.varType);
            }
        }

        switch (type){ // new value
            case("boolean"):
                return (value.equals("true") || value.equals("false") || intPattern.matcher(value).matches()
                        || doublePattern.matcher(value).matches());
            case("int"):
                return (intPattern.matcher(value).matches());
            case("double"):
                return (doublePattern.matcher(value).matches() || intPattern.matcher(value).matches());
            case("char"):
                return (charPattern.matcher(value).matches());

            case("String"):
                return (strPattern.matcher(value).matches());
            default:
                throw new sJavaException("non existing type");
        }
    }


    /**
     * recives types of 2 variables and returns if the types are compatible or not.
     * @param firstVarType - type of first variable (right hand to the "=", fir example if we are checking
     * the assignment a=b, first var is a).
     * @param otherVarType - type of the second variable.
     * @return true iff the types are compatible
     * @throws sJavaException if an unrecognized type is received.
     */
    private boolean checkTypeAssignment(String firstVarType, String otherVarType) throws sJavaException {
        switch (firstVarType){
            case("int"):
                return (otherVarType.equals("int"));
            case("double"):
                return (otherVarType.equals("int") || otherVarType.equals("double"));

            case("boolean"):
                return (otherVarType.equals("int") || otherVarType.equals("double") ||
                        otherVarType.equals("boolean"));

            case("String"):
                return (otherVarType.equals("String"));

            case("char"):
                return (otherVarType.equals("char"));

            default:
                throw new sJavaException("unrecognized type");
        }
    }



}
