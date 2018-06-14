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

        }
    }

    private String getType(String line){
        int endIndex = line.indexOf(" "); // index the first word ends with
        return (line.substring(0,endIndex));
    }


    private boolean isFinal(String var){ return var.startsWith("final "); }

    private boolean checkAssignment(String var){ return var.contains("=");}

    /**
     * Gets line, finds variables name ans checks if it is legal.
     * @param var
     * @return
     */
    private String checkName(String var){

    }
    /**
     * Check if there are more then one variables declared in one line, if so splits line and adds both to
     * list of vars.
     * @param line - line in which there night be more then one var  declared.
     * @return true if there multipule vars that were created succesfully, false otherwise.
     */
    private boolean checkMiltpuleVars(String line){
        if(line.contains(",")){

            String type = getType(line);
            String[] vars = line.split(",");
            for (String var: vars){
                String name ;
                Variable newVar = new Variable(type,name,checkAssignment(var),isFinal(var));
                variables.put(name,newVar);
            }

        }
    }






}
