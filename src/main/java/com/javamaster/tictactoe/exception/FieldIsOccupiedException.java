package com.javamaster.tictactoe.exception;

public class FieldIsOccupiedException extends  Exception {
    private String message;

    public FieldIsOccupiedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
