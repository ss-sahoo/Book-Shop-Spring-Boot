package com.library.management.exception;

/**
 * User Not Found Exception
 * 
 * This exception is thrown when a requested user is not found in the system.
 * It extends RuntimeException, making it an unchecked exception.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructor with message
     * 
     * @param message the error message
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * 
     * @param message the error message
     * @param cause the cause of the exception
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

