package com.loko.domain.exception;

public class UnauthorizeException extends RuntimeException {
    public UnauthorizeException(String message){
        super(message);
    }
}
