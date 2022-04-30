package com.javamaster.tictactoe.exception;

public class GameNotFoundExeception extends Exception{
    private String message;

    public GameNotFoundExeception(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
