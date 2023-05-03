package com.isoft;

import com.isoft.expression.MathExpression;

import java.util.Scanner;

import static java.lang.System.err;
import static java.lang.System.out;

public class App {
    public static void main(String[] args) throws IllegalAccessException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            out.println("Enter the math expression or type exit to exit the program : (valid tokens:\n(,)\n,{,}\n,[,]\n,+,-,/,*,^,\nlog," +
                    "tan," +
                    "cot," +
                    "cos," +
                    "sin," +
                    "sqrt," +
                    "exp," +
                    "max," +
                    "min," +
                    "ln," +
                    "mod," +
                    "!," +
                    "abs");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit"))
                break;
            try {
                String result = MathExpression.evaluate(input);
                out.println(" = " + result);
            } catch (Exception e) {
                err.println(e.getMessage());
            }
        }
    }
}
