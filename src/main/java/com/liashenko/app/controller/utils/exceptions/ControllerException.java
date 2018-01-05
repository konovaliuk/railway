package com.liashenko.app.controller.utils.exceptions;

public abstract class ControllerException extends RuntimeException{
    public ControllerException() {
    }

    public ControllerException(String s) {
        super(s);
    }
}
