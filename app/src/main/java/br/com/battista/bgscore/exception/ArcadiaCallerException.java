package br.com.battista.bgscore.exception;


import java.io.Serializable;

public class ArcadiaCallerException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public ArcadiaCallerException(String message) {
        super(message);
    }

    public ArcadiaCallerException(String message, Throwable cause) {
        super(message, cause);
    }
}
