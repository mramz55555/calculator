package com.isoft;

import com.isoft.expression.MathExpression;
import com.isoft.expression.Tokenizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Math.*;

public class MathExpressionTest {

    private final String firstExpression = "{a+12*(31-(99*13+[12-314-98/12])-b)}";
    private final String expression = "{a+(12*(31-((99*13)+[12-(314-(98/12))])-b))}";
    private final String thirdExpression = "    (10+(-12*(31-((99*13)+(12-(314-(98/+12))))-314)))";
    private final String $4thExpression = "31-(99*13+(12-(314-98/+12))-314)";
    private final String withMaxFunc = "41+(-3.65*ln(cos(13))*max(exp(1-9*(-31)),53*42,log(sin(6)^2)^3))";

    @Test
    void isParenthesisMatch() {
        Assertions.assertFalse(MathExpression.isParenthesisMatch("(a +12 *(31-(99*13/12)-b)"));
        Assertions.assertTrue(MathExpression.isParenthesisMatch(expression));
    }

    @Test
    void toPostfix() throws IllegalAccessException {
        Assertions.assertEquals("a 12 31 99 13 *12 +314 -98 12 /--b -*+", MathExpression.toPostfix(expression));
        Assertions.assertEquals("a 12 31 99 13 *12 +314 98 12 /---b -*+",
                MathExpression.toPostfix(expression));
    }

    @Test
    void tokenzie() throws IllegalAccessException {
        Tokenizer tokenizer = new Tokenizer();
        Assertions.assertEquals(
                "[41, +, (, -3.65, *, ln, (, cos, (, 13, ), ), *, max, (, exp, (, 1, -, 9, *, -, 31, ), ,, 53, *, 42, ,, log, (, sin, (, 6, ), ^, 2, ), ^, 3, ), )]", tokenizer.tokenize(withMaxFunc).toString());
    }

    @Test
    void evaluate() throws IllegalAccessException {
        Assertions.assertEquals(41 + (-3.65 * log(cos(toRadians(13))) * max(max(exp(1 - 9 * -31), 53 * 42), pow(log10(pow(sin(toRadians(6)), (double) 2)), 3))) + "",
                MathExpression.evaluate(withMaxFunc));

//        Assertions.assertEquals(MathExpression.evaluate(
//                42+(-377+log10(log10(+Math.pow(9,Math.pow(2,2))))*(+sin(Math.toRadians(31+(max(93.6*1-log(100),exp(Math.pow(sin(Math.toRadians(1)),Math.pow(sin(Math.toRadians(1)),2)))))))+
//                        pow(cos(1),2))-1/tan(Math.toRadians(1-tan(Math.toRadians(pow(2,4/3))))/(164+39)/(90.131414-76)*sin(Math.toRadians(34)))),
//        "42+(-377+log(log(+9^2^2))*{+sin(31+[max(93.6*1-ln(100),exp(sin^2(1)),cos^2(1))-cot(1-tan(2^4/3))])/[164+39]/[90.131414-76]*sin(34)})"));
//        Assertions.assertThrows(IllegalArgumentException.class,()->MathExpression.evaluate(". 131 . 31*31"));
//        Assertions.assertEquals(log10(31)*(12.313/(5435.908-141.7414))+"",MathExpression.evaluate("log(31)*(12.313/(5435.908-141.7414))"));
//        Assertions.assertEquals(log10(31)*(12.313/(exp(5.908)-141.7414))+"",MathExpression.evaluate("log(31)*(12.313/(exp(5.908)-141.7414))"));
//        Assertions.assertThrows(IllegalArgumentException.class,()->MathExpression.evaluate("*912"));
//        Assertions.assertThrows(IllegalArgumentException.class,()->MathExpression.evaluate(-648.1666666666667 + ""));
//        Assertions.assertEquals("", MathExpression.evaluate("313+"));
//        Assertions.assertThrows(IllegalArgumentException.class,()-> MathExpression.evaluate("31+"));
//        Assertions.assertEquals( -357.5795438, MathExpression.evaluate("42+(-377  + log(-981)*{[164+39]/[90-76]*sin(34)})"));
//        Assertions.assertEquals(42+(-377  + log10(log10(Math.pow(9,Math.pow(2,2))))*(+sin(Math.toRadians(31+(93.6342*1/tan(Math.toRadians(
//                1-tan(Math.toRadians(pow(2,(double) 4/3))))/(164+39)/(90.131414-76)*sin(Math.toRadians(34)))))))),
//                Double.parseDouble(
//                        MathExpression.evaluate("42+(-377  + log(log(+9^2^2))*{+sin(31+[93.6342*cot(1-tan(2^4/3))])/[164+39]/[90.131414-76]*sin(34)})")));
//        Assertions.assertEquals("", MathExpression.evaluate("og + 12"));
//        Assertions.assertEquals(43, MathExpression.evaluate("12 +  31"));
    }

    @Test
    void validateEx() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isValidMethod = MathExpression.class.getDeclaredMethod("isValidExpression", AtomicReference.class);
        isValidMethod.setAccessible(true);
        Assertions.assertTrue((Boolean) isValidMethod.invoke(MathExpression.class, new AtomicReference<>(expression)));
        Assertions.assertFalse((Boolean) isValidMethod.invoke(MathExpression.class, new AtomicReference<>("{a+(12*(31((99*13)+[12-(314-(98/12))])-b))}")));
    }
}
