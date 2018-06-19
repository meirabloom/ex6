package oop.ex6;

public class sJavaException extends Exception {

    /**
     * Constructor for sJava exception
     * @param msg - error message to display
     */
    sJavaException(String msg){
        super(msg);
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

}
