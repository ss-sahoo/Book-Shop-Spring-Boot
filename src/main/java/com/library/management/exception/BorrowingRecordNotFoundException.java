package com.library.management.exception;

/**
 * Borrowing Record Not Found Exception
 * 
 * This exception is thrown when a requested borrowing record is not found in the system.
 * It extends RuntimeException, making it an unchecked exception.
 */
public class BorrowingRecordNotFoundException extends RuntimeException {

    /**
     * Constructor with message
     * 
     * @param message the error message
     */
    public BorrowingRecordNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * 
     * @param message the error message
     * @param cause the cause of the exception
     */
    public BorrowingRecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

