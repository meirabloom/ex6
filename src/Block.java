import java.util.HashMap;


public class Block {

    Block parent;
    HashMap<String, String> localVariables;
    public Block(Block parent){
        this.parent = parent;
        localVariables = new HashMap<>();
    }
}
