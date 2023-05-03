package com.isoft.expression;

import com.isoft.list.Stack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static com.isoft.expression.ArithmeticOpToken.MULTIPLICATION;

public class MathExpression {

    static final String EMPTY_STRING = "";
    private static final Tokenizer tokenizer = new Tokenizer();

    public static boolean isParenthesisMatch(String expression) {
        Stack<String> stack1 = new Stack<>(expression.length());

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '{' || c == '[' || c == '(')
                stack1.add(c + EMPTY_STRING);
            else if (c == ')' || c == ']' || c == '}')
                if (stack1.isEmpty())
                    return false;
                else {
                    //match the types of the parenthesis []{}()
                    int top = stack1.peek().charAt(0);
                    if (top + 2 == c || top + 1 == c)
                        stack1.pop();
                    else return false;
                }
        }
        return stack1.isEmpty();
    }


    private static boolean isValidExpression(AtomicReference<String> expression) {
        //2 parenthesised expressions or numbers without any operator between them
        String ex = expression.get();
        if (Pattern.compile("[\\d)\\]}]\\s+[^+\\-^/*]\\s+[({\\[\\d]").matcher(ex).find())
            return false;
        expression.set(ex.replaceAll("\\s+", EMPTY_STRING));
        return isParenthesisMatch(ex) && !Pattern.compile(
                "[^ ,.!bdexpmlogcsintaqr0-9(\\[{)\\]}+\\-*^/]|" + //Illegal char
                        "[]})]\\d|\\d[(\\[{]|" + // (a or LC)n or n(a or RC)
                        "(^|[{\\[(])[*^/]|" + //RC or beginning of the expression op (+ , - are excluded from RC op form second one because they me be unary)
                        "[]})][{\\[(]|" + //LC RC
                        "[+*/^\\-](?![\\demtlsc(\\[{])|" + //operator without any operand after it
//                        "(lo(?!g\\(.+\\)))|" + //incorrect logarithm
                        "((?<!co)s(?!in\\(.+\\)))|" + // '' sin
                        "((?<!co)t(?!an\\(.+\\)))|" + // '' tan
                        "(c(?!o(t|s)\\(.+\\)))|" + // '' cos or cot
                        "(e(?!xp\\(.+\\)))|" + // '' exp
                        "(m(?!(|ax|in)\\(.+(,.+)*)\\))|" + // '' max or min
                        "(l(?!(n|og)\\([^,]+\\)))|" + // '' ln
                        "(sq(?!rt\\([^,]+\\)))|" + // '' sqrt
                        "(abs(?!\\([^,]+\\)))|" + // '' abs
                        "(mod(\\(?!\\d+,\\d+\\)))|" + // '' mod
                        "(?<!\\d)!" // '' factorial
        ).matcher(expression.get()).find();
    }


    public static ArrayList<String> toPostfix(String expression) throws IllegalAccessException {
        AtomicReference<String> ex = new AtomicReference<>(expression);
        if (!isValidExpression(ex))
            throw new IllegalArgumentException("Invalid expression");

        ArrayList<String> tokens = tokenizer.tokenize(ex.get());
        Stack<String> stack1 = new Stack<>(expression.length());
        ArrayList<String> postfix = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            //arithmetic or precedence op
            if (Tokenizer.isNumber(token))
                postfix.add(token);

            else if (Tokenizer.isOperator(token.charAt(0)) || SeparatorToken.isSeparator(token.charAt(0))) {
                //arith/precedence operator or separator or function
                if (!stack1.isEmpty()) {
                    String top = stack1.peek();

                    if (Tokenizer.getPrecedence(top, true) > Tokenizer.getPrecedence(token, false)) {
                        postfix.add(stack1.pop());
                        i--;
                        // Collision of right and left parenthesis.never two different precedence get equal except for the out of the stack of LC and in stack of RC.
                    } else if (Tokenizer.getPrecedence(top, true) == Tokenizer.getPrecedence(token, false)) {
                        stack1.pop();
//                        i--;
                    }
                    //so is it symbol
                    else if (SeparatorToken.isSeparator(token.charAt(0)))
                        postfix.add(token);
                    else
                        stack1.add(token);
                } else
                    stack1.add(token);
            } else if (Character.isLetter(token.charAt(0))) {
                if (!Function.isFunction(token))
                    throw new IllegalArgumentException("Invalid expression");
                stack1.add(token);
            }
        }

        while (!stack1.isEmpty())
            postfix.add(stack1.pop());
        return postfix;
    }

    public static String evaluate(String expression) throws IllegalAccessException {
        ArrayList<String> postfix = toPostfix(expression);
        Stack<String> stack1 = new Stack<>(expression.length());
        List<String> multiVarFuncArgs = new ArrayList<>();
        for (String token : postfix) {

            char firstChar = token.charAt(0);
            if (Tokenizer.isNumber(token))
                stack1.add(token);
                //if this func has multiple args
            else if (SeparatorToken.isSeparator(firstChar))
                multiVarFuncArgs.add(stack1.pop());
                //if it's an arithmetic operator or sign of a function
            else if (ArithmeticOpToken.isArithmeticOpToken(firstChar)) {
                //if it's sign of a function
                if (1 < token.length() && Character.isLetter(token.charAt(1)))
                    stack1.add(ArithmeticOpToken.compute(MULTIPLICATION.getTokenChar() + EMPTY_STRING, (firstChar == '-' ? "-1" : "1"),
                            Function.compute(token, new String[]{"0", stack1.pop()})));
                    //else it's binary operator
                else {
                    String operand2 = stack1.pop();
                    stack1.add(ArithmeticOpToken.compute(token, stack1.pop(), operand2));
                }
            } else if (Function.isFunction(token)) {
                if (Function.isMultiVariable(token)) {
                    multiVarFuncArgs.add(stack1.pop());
                    stack1.add(Function.compute(token, Arrays.copyOf(multiVarFuncArgs.toArray(), multiVarFuncArgs.size(), String[].class)));
                }
                stack1.add(Function.compute(token, new String[]{"0", stack1.pop()}));
            }
        }
        return stack1.pop() + EMPTY_STRING;
    }

}

