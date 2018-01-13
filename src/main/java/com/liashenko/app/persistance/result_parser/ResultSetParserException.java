package com.liashenko.app.persistance.result_parser;

public class ResultSetParserException extends RuntimeException {
    public ResultSetParserException() {
    }

    public ResultSetParserException(String s) {
        super(s);
    }

    public ResultSetParserException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
