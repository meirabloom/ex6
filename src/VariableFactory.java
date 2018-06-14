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

    public HashMap<String, Variable> getVariables() throws sJavaException{
        for (String var : strVars) {
            Pattern general = Pattern.compile("(final\\s+)?\\s*(int|double|String|boolean|char)\\s+(.*)(;)");
            if(!general.matcher(var).matches()){
                throw new sJavaException("illegal variable deceleration format");
            }
            if(isFinal(var) && !isAssigned(var)){
                throw new sJavaException("Unassigned final variable");
            }
            String[] splitLine = var.split(" ");
            LinkedList<String> lineElement = new LinkedList<String>();
            for(String elem:splitLine){
                if(elem!=null){
                    lineElement.add(elem);
                }
            }
            String type = lineElement.getFirst();
            String name = splitLine[1];
            if (var.contains(",")) {
                handleMiltpuleVars(var); // possibly multiple vars
            }
            if (checkName(name)) {
                Variable newVar = new Variable(type, name, isAssigned(var), isFinal(var));
                variables.put(name, newVar);
            }
        }
        return variables;
    }



    private boolean isFinal(String var){ return var.startsWith("final "); }

    private boolean isAssigned(String var){ return var.contains("=");}

    /**
     * Gets line, finds variables name ans checks if it is legal.
     * @param
     * @return
     */
    private boolean checkName(String name){
        Pattern namePattern = Pattern.compile("\\D.*");
        if(namePattern.matcher(name).matches() && !name.equals("_") && !variables.containsKey(name)){
            return true;
        }
        return false;
    }

    /**
     * Check if there are more then one variables declared in one line, if so splits line and adds both to
     * list of vars.
     * @param line - line in which there night be more then one var  declared.
     * @return true if there multipule vars that were created succesfully, false otherwise.
     */
    private boolean handleMiltpuleVars(String line){
        {
            String[] vars = line.split(",");

            for (String var: vars){
                String name ;
                Variable newVar = new Variable(type,name,isAssigned(var),isFinal(var));
                variables.put(name,newVar);
            }


    }






}
