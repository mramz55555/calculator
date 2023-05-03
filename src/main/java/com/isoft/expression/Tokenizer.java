package com.isoft.expression;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.isoft.expression.ArithmeticOpToken.MINUS;
import static com.isoft.expression.ArithmeticOpToken.PLUS;
import static com.isoft.expression.MathExpression.EMPTY_STRING;
import static com.isoft.expression.PrecedenceToken.*;

public class Tokenizer {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger();

    static int getPrecedence(String token1, boolean inStack) throws IllegalAccessException {
        for (Function f : Function.values())
            if (f.getValue().equals(token1))
                return inStack ? f.getInStackPrecedence() : f.getOutOfStackPrecedence();

        Class<?>[] tokenClasses = {PrecedenceToken.class, ArithmeticOpToken.class, SeparatorToken.class};

        for (Class<?> clazz : tokenClasses)
            for (Field field : clazz.getDeclaredFields()) {
                Token token = (Token) field.get(clazz);
                if (token.tokenChar == token1.charAt(0))
                    return inStack ? token.inStackPrecedence : token.outOfStackPrecedence;
            }
        throw new IllegalArgumentException("Illegal token :" + token1);
    }

    static boolean isOperator(char c) throws IllegalAccessException {
        return ArithmeticOpToken.isArithmeticOpToken(c) || PrecedenceToken.isPrecedenceToken(c);
    }

    static boolean isNumber(String str) {
        return Pattern.compile("[-+]?(\\d+|\\d+\\.\\d+)").matcher(str).find();
    }

    public ArrayList<String> tokenize(String expression) throws IllegalAccessException {
        final ArrayList<String> tokens = new ArrayList<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            //precedence operator or arithmetic operator or sign
            if (isOperator(c)) {
                //sign
                if (isSign(i, expression, false)) {
                    tokens.add(parseWSN(i + 1, expression, c + EMPTY_STRING, Character.isDigit(expression.charAt(i + 1))));
                    i = ATOMIC_INTEGER.get();
                } else
                    tokens.add(c + "");
                //symbol or operator or function
            } else if (Character.isLetter(c)) {
                String fun = parseWSN(i, expression, EMPTY_STRING, false);
                i = ATOMIC_INTEGER.get();
                if (!Function.isFunction(fun))
                    throw new IllegalArgumentException("Invalid expression");
                tokens.add(fun);
            } else if (Character.isDigit(c)) {
                tokens.add(parseWSN(i, expression, EMPTY_STRING, true));
                i = ATOMIC_INTEGER.get();
            } else if (SeparatorToken.isSeparator(c))
                tokens.add(c + "");
            else throw new IllegalArgumentException("Invalid expression");
        }
        return tokens;
    }

    private String parseWSN(int position, String str, String sign, boolean isDigit) {
        Predicate<Character> predicate = isDigit ? (c) -> Character.isDigit(c) || c == '.' : Character::isLetter;
        StringBuilder sb = new StringBuilder(sign);
        char c;
        ATOMIC_INTEGER.set(position);
        while (true) {
            c = str.charAt(ATOMIC_INTEGER.get());
            if (!predicate.test(c) || ATOMIC_INTEGER.get() >= str.length())
                break;
            sb.append(c);
            ATOMIC_INTEGER.incrementAndGet();
        }

        if (isDigit && !isNumber(sb.toString()))
            throw new NumberFormatException("Illegal number format");

        ATOMIC_INTEGER.decrementAndGet();
        return sb.toString();
    }

    public boolean isRightCloser(char c) {
        return c == RIGHT_BRACKET.getTokenChar() || c == RIGHT_PARENTHESIS.getTokenChar() || c == RIGHT_CURLY_BRACE.getTokenChar();
    }

    private boolean isSign(int position, String expression, boolean isPostfix) throws IllegalAccessException {
        ATOMIC_INTEGER.set(position);
        char letter = expression.charAt(position);

        if (!isPostfix) {
//            An operator in an infix expression is sign if it:
//            1-Is the first thing in your expression
//            2-comes after another operator
//            3-comes after right parenthesis.

            return (letter == MINUS.getTokenChar() || letter == PLUS.getTokenChar()) && (ATOMIC_INTEGER.get() == 0
                    /*|| (isArithmeticOp(expression.charAt(ATOMIC_INTEGER.get() - 1))*/
                    || isRightCloser(expression.charAt(ATOMIC_INTEGER.get() - 1)));
        }
        //An operator in a postfix is sign if it comes before a number
        return (letter == MINUS.getTokenChar() || letter == PLUS.getTokenChar()) && Character.isLetterOrDigit(expression.charAt(position + 1));
    }
}
