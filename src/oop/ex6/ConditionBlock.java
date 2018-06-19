package oop.ex6;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConditionBlock extends Block {

    //possible conditions
    private static final String INT = "int";
    private static final String BOOLEAN = "boolean";
    private static final String DOUBLE = "double";
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    //regex
    private static final String CONDITION_SIGNATURE = "^\\s*(while|if)\\s*\\((.+)\\)\\s*\\{\\s*";
    private static final String AND_OR = "(&&)|(\\|\\|)";
    private static final String REQUIRED_CONDITION = "\\s*(true|false)|(\\d+\\.?\\d*)|([^\\d\\s]\\S*)";
    private static final String SPECIFIC_CONDITION = "-?\\d+(\\.\\d+)?";

    ConditionBlock(Block parent, LinkedList<String> lines, LinkedList<String> localVariable,
                   HashMap<String, MethodBlock> methods) throws sJavaException {
        super(parent, lines, localVariable, methods);
        verifyCondition();
    }

    @Override
    public String getName() {
        return "condition";
    }

    private void verifyCondition() throws sJavaException {
        String[] multipleConditions;
        String conditionLine;
        String conditionSignature = lines.getFirst();
        conditionLine = conditionSignature.substring((conditionSignature.indexOf("(") + 1),
                conditionSignature.indexOf(")"));
        multipleConditions = conditionLine.trim().split(AND_OR);
        Pattern p = Pattern.compile(REQUIRED_CONDITION);
        for (String condition : multipleConditions) {
            Matcher m = p.matcher(condition);

            if (!m.matches()) {
                throw new sJavaException("Illegal condition");
            }
            condition = condition.trim();
            p = Pattern.compile(SPECIFIC_CONDITION);
            m = p.matcher(condition);

            if (!condition.equals(TRUE) && !condition.equals(FALSE) && !m.matches()) {

                Variable variable = searchForVar(condition);

                if (variable != null && variable.assigned) {
                    String varType = variable.varType;
                    if (!varType.equals(INT) && !varType.equals(DOUBLE) && !varType.equals(BOOLEAN)) {
                        throw new sJavaException("illegal condition variable assignment");
                    }
                } else {
                    throw new sJavaException("condition variable not assigned");
                }
            }
        } //TODO -- what if the condition was empty?
        verifyEnd();
    }

    /**
     * verifies end of if while block
     * @throws sJavaException
     */
    void verifyEnd() throws sJavaException {
        if (!lines.getLast().trim().equals("}")) {
            throw new sJavaException("condition block doesn't end in }");
        }
    }
}
