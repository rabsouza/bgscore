package br.com.battista.bgscore.exception;


import java.io.Serializable;

public class BGScoreException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public BGScoreException(String message) {
        super(message);
    }

    public BGScoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
