package com.CheritSolutions.Business.exception;


public class SlotConflictException extends RuntimeException {

    // Constructor accepting a message
    public SlotConflictException(String message) {
        super(message);  // Pass the message to the parent class (RuntimeException)
    }

    // Optional: You can add another constructor to pass a cause (Throwable) as well
    public SlotConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}

