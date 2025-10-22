package com.library.management.exception;

/**
 * Book Not Found Exception
 * 
 * This exception is thrown when a requested book is not found in the system.
 * It extends RuntimeException, making it an unchecked exception.
 * 
 * Unchecked exceptions don't need to be declared in method signatures
 * and don't need to be caught by the caller.
 */
public class BookNotFoundException extends RuntimeException {

    /**
     * Constructor with message
     * 
     * @param message the error message
     */
    public BookNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * 
     * @param message the error message
     * @param cause the cause of the exception
     */
    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

