package com.isoft.expression;

import java.lang.reflect.Field;
import java.util.List;

public class PrecedenceToken extends Token {
    public static final Token RIGHT_PARENTHESIS = new PrecedenceToken('(', 9, -1);
    public static final Token RIGHT_CURLY_BRACE = new PrecedenceToken('{', 9, -1);
    public static final Token RIGHT_BRACKET = new PrecedenceToken('[', 9, 0);
    public static final Token LEFT_PARENTHESIS = new PrecedenceToken(')', -1, -2);
    public static final Token LEFT_CURLY_BRACE = new PrecedenceToken('}', -1, -2);
    public static final Token LEFT_BRACKET = new PrecedenceToken(']', -1, -2);

    PrecedenceToken(char tokenChar, int outOfStackPrecedence, int inStackPrecedence) {
        super(tokenChar, outOfStackPrecedence, inStackPrecedence);
    }

    static boolean isPrecedenceToken(char c) throws IllegalAccessException {
        for (Field field : List.of(PrecedenceToken.class.getDeclaredFields()))
            if (((Token) field.get(PrecedenceToken.class)).getTokenChar() == c)
                return true;

        return false;
    }

    public char getTokenChar() {
        return tokenChar;
    }
}
