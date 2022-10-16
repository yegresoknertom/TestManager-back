package org.testmanager.exception;

public class FreeUserNotFoundException extends RuntimeException{

    public FreeUserNotFoundException() {
        super("Free user not found");
    }

}
