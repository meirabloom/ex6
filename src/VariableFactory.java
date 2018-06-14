import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VariableFactory {

    LinkedList<String> strVars;
    HashMap<String, Variable> variables;
    VariableFactory (LinkedList<String> strVars){
        this.strVars = strVars;
    }

    public HashMap<String, Variable> getVariables(){
        for (String var: strVars) {
            boolean finalVar = isFinal(var);
            String type = getType(var);
            switch (type){
                case(): // int

                case(): // double

                case(): // String

                case(): // boolean

                case(): //char

                default: // illegal type
            }
        }
    }

    private String getType(String line){
        int endIndex = line.indexOf(" "); // index the first word ends with
        return (line.substring(0,endIndex));
    }


    private boolean isFinal(String var){ return var.startsWith("final "); }

    boolean checkAssignment(String var){ return var.contains("=");}

    /**
     * Check if there are more then one variables declared in one line, if so splits line and adds both to
     * list of vars.
     * @param line - line in which there night be more then one var  declared.
     */
    void checkMiltpuleVars(String line){
        if(line.contains(",")){
            String type = getType(line);
            String[] vars = line.split(",");
            for (String var: vars){
                // create new variable with type "type"
            }

        }
    }





}
