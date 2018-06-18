package oop.ex6;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VariableFactory {

    private static final String LEGAL_PATTERN = "(final\\s+)?\\s*(int|double|String|boolean|char)\\s+(.*)(;)";
    private static final String DOUBLE_PATTERN = "\\d+\\.?\\d*";
    private static final String INT_PATTERN = "\\d+";
    private static final String STR_PATTERN = "\"[a-zA-Z]+\"";
    private static final String CHAR_PATTERN = "\"[a-zA-Z]\"";
    private static final String NAME_PATTERN = "[^\\d\\s]\\S*";
    private static final String NEW_VAL_PATTERN = "(\\d+\\.?\\d*)|(\"[a-zA-Z]+\")";
    //private static final String ASSIGNED_PATTERN = ".*";
    private static final String ASSIGNED_PATTERN = "(\\w+)\\s*(=)\\s*(.*)";
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
     * Constructor
     * @param strVars
     * @param block
     */
    VariableFactory (LinkedList<String> strVars, Block block){
        this.strVars = strVars;
        this.variables = new HashMap<String,Variable>();
        this.block = block;
    }


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
                if (newMatcher.matches()) {
                    String name = newMatcher.group(NAME_PLACE).trim();

                    if (!checkName(name)) {
                        throw new sJavaException("illegal name");
                    }
                    if (newMatcher.matches()) { // oneVar is assigned
                        String value = newMatcher.group(VALUE_PLACE);
                        if(!value.contains("\"")){ // assignment to existing object
                            // value = getVar(value).getValue()
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
                        Variable newVar = new Variable(type, name, UNASSIGNED, isFinal, null);
                        variables.put(name, newVar);
                    }
                }
            }
        }
        return variables;
    }

    private boolean isFinal(String var){ return var.startsWith("final "); }


    /**
     *
     * @param
     * @return
     */
    private boolean checkName(String name) {
        Pattern namePattern = Pattern.compile(NAME_PATTERN);
        if (namePattern.matcher(name).matches() && !name.equals("_") && !variables.containsKey(name)) {
            return true;
        }
        return false;
    }

    /**
     * Checks that the value of the new variable is compatible to the type.
     * @param type - type of the variable
     * @param value - value of the variable
     * @return true if the type is compatible, false otherwise.
     * @throws sJavaException - if the type is unrecognized
     */
    private boolean checkValue(String type, String value) throws sJavaException{ //TODO: check if val exists
        Pattern doublePattern = Pattern.compile(DOUBLE_PATTERN);
        Pattern intPattern = Pattern.compile(INT_PATTERN);
        Pattern strPattern = Pattern.compile(STR_PATTERN);
        Pattern charPattern = Pattern.compile(CHAR_PATTERN);
        Pattern assignToNewValPattern = Pattern.compile(NEW_VAL_PATTERN);
        if(!assignToNewValPattern.matcher(value).matches()){ // assignment to existing variable
            if(variables.containsKey(value)){
                return variables.get(value).varType.equals(type);
            }
            Variable var = block.searchForVar(value);
            if(var!=null){
                return var.varType.equals(type);
            }
        }

        switch (type){
            case("boolean"):
                return (value.equals("true") || value.equals("false"));
            case("int"):
                return (intPattern.matcher(value).matches());
            case("double"):
                return (doublePattern.matcher(value).matches());
            case("char"):
                return (charPattern.matcher(value).matches());

            case("String"):
                return (strPattern.matcher(value).matches());
            default:
                throw new sJavaException("non existing type");
        }

    }

    private boolean checkAssignmentToExistingVar(String name) throws sJavaException {
        Variable var = block.searchForVar(name);
        if (var == null) {
            throw new sJavaException("assignment to non existing variable");
        } else {
            if (!var.assigned) {
                throw new sJavaException("assignment to unassigned variable");
            }
        }
        return true;
    }

}
