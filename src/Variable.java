
public class Variable {
    String varType;
    String name;
    boolean assigned;
    boolean isFinal;

    Variable(String type, String name, boolean assigned, boolean isFinal){
        this.varType = type;
        this.name = name;
        this.assigned = assigned;
        this.isFinal = isFinal;
        }
}
