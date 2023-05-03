package com.isoft.expression;

public abstract class Token {
    protected final int outOfStackPrecedence;
    protected final int inStackPrecedence;
    protected final char tokenChar;


    Token(char tokenChar, int outOfStackPrecedence, int inStackPrecedence) {
        this.tokenChar = tokenChar;
        this.outOfStackPrecedence = outOfStackPrecedence;
        this.inStackPrecedence = inStackPrecedence;
    }

    public char getTokenChar() {
        return tokenChar;
    }

    public int getOutOfStackPrecedence() {
        return outOfStackPrecedence;
    }

    public int getInStackPrecedence() {
        return inStackPrecedence;
    }

}
