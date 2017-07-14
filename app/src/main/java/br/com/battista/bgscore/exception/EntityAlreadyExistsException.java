package br.com.battista.bgscore.exception;

import java.io.Serializable;

public class EntityAlreadyExistsException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public EntityAlreadyExistsException(String s) {
        super(s);
    }

    public EntityAlreadyExistsException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
