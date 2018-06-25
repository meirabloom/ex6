package oop.ex6;

/**
 * receives a string specifying the reason for the error, and throws an exception with the explanation message
 */
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
