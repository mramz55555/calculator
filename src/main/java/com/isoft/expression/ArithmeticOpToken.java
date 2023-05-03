package com.isoft.expression;

import java.lang.reflect.Field;
import java.util.List;

public class ArithmeticOpToken extends Token {
    public static final Token PLUS = new ArithmeticOpToken('+', 1, 2);
    public static final Token MINUS = new ArithmeticOpToken('-', 1, 2);
    public static final Token MULTIPLICATION = new ArithmeticOpToken('*', 3, 4);
    public static final Token DIVISION = new ArithmeticOpToken('/', 3, 4);
    // because of associativity. L to R precedence increases.R to L decreases
    public static final Token POWER = new ArithmeticOpToken('^', 6, 5);

    public ArithmeticOpToken(char tokenChar, int outOfStackPrecedence, int inStackPrecedence) {
        super(tokenChar, outOfStackPrecedence, inStackPrecedence);
    }

    static boolean isArithmeticOpToken(char c) throws IllegalAccessException {
        for (Field field : List.of(ArithmeticOpToken.class.getDeclaredFields()))
            if (((Token) field.get(ArithmeticOpToken.class)).getTokenChar() == c)
                return true;
        return false;
    }

    public static String compute(String operator, String operand1, String operand2) {
        double val1 = Double.parseDouble(operand1);
        double val2 = Double.parseDouble(operand2);

        char op = operator.charAt(0);

        String result = String.valueOf(
                op == PLUS.tokenChar ? val1 + val2 :
                        op == MINUS.tokenChar ? val1 - val2 :
                                op == DIVISION.tokenChar ? val1 / val2 :
                                        op == MULTIPLICATION.tokenChar ? val1 * val2 :
                                                op == POWER.tokenChar ? Math.pow(val1, val2) : "");

        if (result.equals("NaN"))
            throw new ArithmeticException("Invalid expression");
        if (result.equals("Infinity") || result.equals("-Infinity"))
            throw new ArithmeticException(result + " value");
        return result;
    }
}
