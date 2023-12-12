package org.example;



import org.example.calculator.Calculator;
import org.example.exceptions.DividedByZero;
import org.example.exceptions.IncorrectExpressionException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        Calculator calculator = new Calculator();

        System.out.print("Enter here your arithmetical expression: ");
        String expr = in.nextLine();

        try {
            int result = calculator.calculate(expr);
            System.out.println("Result: " + result);
        } catch (IncorrectExpressionException | DividedByZero e) {
            throw new RuntimeException(e);
        }
    }
}