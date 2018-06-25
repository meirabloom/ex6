package oop.ex6;

/**
 * a class representing the variable objects
 */
public class Variable {
    String varType;
    String name;
    boolean assigned;
    boolean isFinal;
    boolean isGlobal;
    String value;

    /**
     * Constructor for variable
     * @param type - variable type
     * @param name - variable name
     * @param assigned - true iif variable is assigned
     * @param isFinal - true iff variable is final
     * @param value - value of the variable if it is assigned, null if it isn't.
     */
    Variable(String type, String name, boolean assigned, boolean isFinal, String value){
        this.varType = type;
        this.name = name;
        this.assigned = assigned;
        this.isFinal = isFinal;
        this.value = value;
        this.isGlobal = false;
        }

    /**
     * Set value of variable and changes assigned to true
     * @param newValue - new value to set
     */
    public void setValue(String newValue){
        value = newValue;
        assigned = true;
    }

    /**
     * Set variable to be global
     */
    public void setGlobal(){
        isGlobal = true;
    }

}

