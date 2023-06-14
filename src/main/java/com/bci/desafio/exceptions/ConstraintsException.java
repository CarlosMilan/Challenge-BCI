package com.bci.desafio.exceptions;

public class ConstraintsException extends RuntimeException{
    private int code;
    public ConstraintsException(int code, String message) {
        super(message);
        this.code = code;
    }
    public ConstraintsException(String message) {
        super(message);
    }
    public int getCode() {
        return code;
    }
}