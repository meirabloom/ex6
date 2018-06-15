import java.util.HashMap;
import java.util.LinkedList;


public abstract class Block {

    Block parent;
   // HashMap<String, Variable> localVariables;
    LinkedList<String> lines;


    public Block(Block parent, LinkedList<String> lines, LinkedList<String> variables){
   //     this.parent = parent;
   //     VariableFactory varFactory = new VariableFactory(variables);
   //     localVariables = varFactory.getVariables();
   //     this.lines = lines;
    }


}
