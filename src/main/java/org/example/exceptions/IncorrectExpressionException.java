package org.example.exceptions;


import org.example.calculator.AllowedOperations;

import java.util.List;

public class IncorrectExpressionException extends Exception{

    public IncorrectExpressionException(){
        super("Expression is incorrect! Be sure use numbers and one of the following operators: " + AllowedOperations.getAllowedOperations().replaceAll("\\\\", ""));
    }
}
