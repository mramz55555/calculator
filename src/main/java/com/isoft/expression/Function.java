package com.isoft.expression;

import java.util.Arrays;
import java.util.stream.IntStream;

public enum Function {
    LOG("log"),
    TAN("tan"),
    COT("cot"),
    COS("cos"),
    SIN("sin"),
    //    ATAN("atan"),
//    ACOT("acot"),
//    ASIN("asin"),
//    ACOS("acos"),
//    TANH("tanh"),
//    COTH("coth"),
//    SINH("sinh"),
//    COSH("cosh"),
    SQRT("sqrt"),
    EXP("exp"),
    MAX("max"),
    MIN("min"),
    LN("ln"),
    MOD("mod"),
    FACTORIAL("!"),
    ABS("abs");

    private static final int OUT_OF_STACK_PRECEDENCE = 8, IN_STACK_PRECEDENCE = 7, ONE = 1;
    private final String value;
    private final int outOfStackPrecedence;
    private final int inStackPrecedence;

    Function(String function) {
        this.value = function;
        this.outOfStackPrecedence = OUT_OF_STACK_PRECEDENCE;
        this.inStackPrecedence = IN_STACK_PRECEDENCE;
    }

    static boolean isMultiVariable(String func) {
        return func.equals(MAX.value) || func.equals(MIN.value) || func.equals(MOD.value);
    }

    public static boolean isFunction(String func) {
        return Arrays.stream(Function.values()).anyMatch(f -> f.getValue().equals(func));
    }

    public static String compute(String operator, String[] args) {
        double[] args1 = new double[args.length];

        for (int i = 0; i < args.length; i++)
            args1[i] = Double.parseDouble(args[i]);

        String result;
        if (isMultiVariable(operator))
            result = String.valueOf(operator.equals(MAX.value) ?
                    Arrays.stream(args1).reduce(Double.MIN_VALUE, Math::max)
                    : operator.equals(MOD.value) ? args1[0] % args1[1]
                    : Arrays.stream(args1).reduce(Double.MAX_VALUE, Math::min));
        else
            result = String.valueOf(
                    operator.equals(EXP.value) ? Math.exp(args1[ONE])
                            : operator.equals(FACTORIAL.value) ? IntStream.range(2, (int) args1[ONE] + 1).reduce(1, (a, b) -> a * b)
                            : operator.equals(LN.value) ? Math.log(args1[ONE])
                            : operator.equals(ABS.value) ? Math.abs(args1[ONE])
                            : operator.equals(LOG.value) ? Math.log10(args1[ONE])
                            : operator.equals(SQRT.value) ? Math.sqrt(args1[ONE])
//                        : operator.equals(ATAN.value) ? Math.atan(args1[ONE])
//                        : operator.equals(ACOT.value) ? 1/Math.atan(args1[ONE])
//                        : operator.equals(ASIN.value) ? Math.asin(args1[ONE])
//                        : operator.equals(ACOS.value) ? Math.acos(args1[ONE])
//                        : operator.equals(SINH.value) ? Math.sinh(args1[ONE])
//                        : operator.equals(COSH.value) ? Math.cosh(args1[ONE])
//                        : operator.equals(TANH.value) ? Math.tanh(args1[ONE])
//                        : operator.equals(COTH.value) ? 1/Math.tanh(args1[ONE])
                            : operator.equals(SIN.value) ? Math.sin(Math.toRadians(args1[ONE]))
                            : operator.equals(COS.value) ? Math.cos(Math.toRadians(args1[ONE]))
                            : operator.equals(COT.value) ? 1 / Math.tan(Math.toRadians(args1[ONE]))
                            : operator.equals(TAN.value) ? Math.tan(Math.toRadians(args1[ONE])) : "");


        if (result.equals("NaN"))
            throw new ArithmeticException("Invalid expression");
        if (result.equals("Infinity") || result.equals("-Infinity"))
            throw new ArithmeticException(result + " value");
        return result;
    }

    public String getValue() {
        return value;
    }

    public int getOutOfStackPrecedence() {
        return outOfStackPrecedence;
    }

    public int getInStackPrecedence() {
        return inStackPrecedence;
    }
}
