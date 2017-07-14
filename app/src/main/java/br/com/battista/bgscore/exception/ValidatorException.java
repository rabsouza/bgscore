package br.com.battista.bgscore.exception;

import java.io.Serializable;

public class ValidatorException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
