import java.util.HashMap;
import java.util.LinkedList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class temp {
    public static void main(String[] args) {
        try {
            LinkedList<String> s = new LinkedList<String>();
            s.add("final int a=9;");
          //  s.add("double b = 5.9;");
            //s.add("char t = ''c''");
            VariableFactory vf = new VariableFactory(s, null);
            HashMap<String, Variable> t = vf.getVariables();
            System.out.println(t.toString());


        } catch (sJavaException s) {
            System.out.println("error");
        }
    }
}
