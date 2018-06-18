package oop.ex6;

import java.util.LinkedList;

public class ConditionBlock extends Block {

    ConditionBlock(Block parent, LinkedList<String> lines, LinkedList<String> localVariable) throws sJavaException {
        super(parent, lines,localVariable);
    }

    @Override
    public String getName() {
        return "condition";
    }
}
