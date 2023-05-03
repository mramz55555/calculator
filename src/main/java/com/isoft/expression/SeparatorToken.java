package com.isoft.expression;

public class SeparatorToken extends Token {
    public static final Token COMMA = new SeparatorToken(',', 0, -1);

    SeparatorToken(char tokenChar, int outOfStackPrecedence, int inStackPrecedence) {
        super(tokenChar, outOfStackPrecedence, inStackPrecedence);
    }

    static boolean isSeparator(char c) {
        return c == ',';
    }
}
