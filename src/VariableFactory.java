import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VariableFactory {

    LinkedList<String> strVars;
    LinkedList <Variable> variables;
    VariableFactory (LinkedList<String> strVars){
        this.strVars = strVars;
    }

    LinkedList <Variable> getVariables(){
        for (String var: strVars){
            switch (var){
                case(): // int

                case(): // double

                case(): // String

                case(): // boolean

                case(): //char

                default: // illegal type
            }
        }
    }


}
