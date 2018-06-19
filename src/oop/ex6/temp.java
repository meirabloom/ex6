
package oop.ex6;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class temp {
    public static void main(String[] args) {

        try {
            LinkedList<String> s = new LinkedList<String>();
            s.add("if(true){");
            s.add("int k = 8;");
            s.add("double r = 8.9086;");
            s.add("double b = r;");

            LinkedList<String> m = new LinkedList<String>();
            m.add("void bla(int a){");
            m.add("int k = 8;");
            m.add("double a = 8.9086;");
            m.add("double b = r;");

            LinkedList<String> my = new LinkedList<String>();
            my.add("int k = 8;");
            my.add("double a = 8.9086;");
            my.add("double b = r;");
            GlobalBlock gb = new GlobalBlock(s);

            ConditionBlock cb = new ConditionBlock(gb,s,my);
            cb.getName();
            MethodBlock mb = new MethodBlock(gb,m,my);

           // String s = "(void)\\s+([a-zA-z]\\w*)\\s*+\\((.*)\\)\\s*{";

            VariableFactory vf = new VariableFactory(s, mb);
            HashMap<String, Variable> t = vf.getVariables();
            System.out.println(t.toString());

        } catch (sJavaException s) {
            System.out.println("error");
        }
    }

    int foo(int a){
        return a;
    }




}