package oop.ex6;

public class Variable {
    String varType;
    String name;
    boolean assigned;
    boolean isFinal;
    boolean isGlobal;
    String value;

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
