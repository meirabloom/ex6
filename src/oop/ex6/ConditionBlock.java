package oop.ex6;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConditionBlock extends Block {

    //possible conditions
    private static final String INT = "int";
    private static final String BOOLEAN = "boolean";
    private static final String DOUBLE = "double";

    //regex
    static final String CONDITION_SIGNATURE = "^\\s*(while|if)\\s*\\((.+)\\)\\s*\\{\\s*";
    static final String AND_OR = "(&&)|(\\|\\|)";

    ConditionBlock(Block parent, LinkedList<String> lines, LinkedList<String> localVariable)
            throws sJavaException {
        super(parent, lines,localVariable);
    }

    @Override
    public String getName() {
        return "condition";
    }

    //TODO get the condition, check if its valid- if it says true/ false/ a number/ an existing variable

    private void extractCondition(){
        Pattern p = Pattern.compile(CONDITION_SIGNATURE);
        Matcher m = p.matcher(lines.getFirst());
        if(m.matches()) {
            String condition = m.group(2);
            String[] multipleConditions = condition.trim().split(AND_OR);
        }

    }

    public boolean verifyCondition
}
