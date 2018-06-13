import java.util.HashMap;
import java.util.LinkedList;


public abstract class Block {

    Block parent;
    HashMap<String, String> localVariables;
    LinkedList<String> lines;

    public Block(Block parent, LinkedList<String> lines, LinkedList<String> variables){
        this.parent = parent;
        localVariables = VariableFactory(variables);
        this.lines = lines;
    }


}
