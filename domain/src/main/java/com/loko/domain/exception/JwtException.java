package com.loko.domain.exception;

public class JwtException extends RuntimeException {
    private int status;

   

    public JwtException(String message) {
        super(message);
    }

    public JwtException(String message, int status) {
        super(message);
        this.status = status;
    }

    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
