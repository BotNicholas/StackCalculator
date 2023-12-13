package org.example.calculator;

import org.example.exceptions.DividedByZero;
import org.example.exceptions.IncorrectExpressionException;

import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private final Stack<Character> operations;
    private final Stack<Integer> numbers;

    public Calculator(){
        operations = new Stack<>();
        numbers = new Stack<>();
    }

    //Method that calculates result from converted line
    public int compute(String line) throws DividedByZero {
        List<String> elements = List.of(line.split(","));

        for (String e : elements) {
            if(e.matches("\\d+")){
                numbers.push(Integer.parseInt(e));
            } else {
                Integer n1, n2;
                switch(e){
                    case "+":
                        n1 = numbers.pop();
                        n2 = numbers.pop();
                        numbers.push(n2+n1);
                        break;
                    case "-":
                        n1 = numbers.pop();
                        n2 = numbers.pop();
                        numbers.push(n2-n1);
                        break;
                    case "*":
                        n1 = numbers.pop();
                        n2 = numbers.pop();
                        numbers.push(n2*n1);
                        break;
                    case "/":
                        n1 = numbers.pop();
                        if(n1==0)
                            throw new DividedByZero();
                        n2 = numbers.pop();
                        numbers.push(n2/n1);
                        break;
                }
            }
        }
        return numbers.pop();
    }

    //Method that converts simple expression string into reversed Polish one
    public String convert(String expr){
        char prev = 'c';
        int prevPr=0;
        StringBuilder result = new StringBuilder();

        //converting process
        for(int i=0; i<expr.length(); i++){
            if(Character.isDigit(expr.charAt(i))){ //if it is number add it to the resulting string
                result.append(expr.charAt(i));
                prev='n';

            } else { //if it is an operation
                if(expr.charAt(i)!='(' && expr.charAt(i)!=')')
                    result.append(",");

                //if current symbol is ')' extract all the operations till '('
                if(expr.charAt(i)==')'){
                    char tmp = operations.pop();
                    while(tmp != '('){
                        result.append(","+tmp);
                        tmp=operations.pop();
                    }
                    prevPr= operations.empty() ? 0 : getPriority(operations.peek());
                } else {
                    /**
                     * Special cases handler:

                     * Note 1: '-' operation is unique, thus if current or previous operation in stack is '-' we have to work it different...
                     * Note 2: if current and previous operations are the same, it is also a special case
                     * Note 3: however we have to keep operations priority order!
                     */
                    if ((expr.charAt(i) == '-')
                            || (!operations.empty() && operations.peek() == expr.charAt(i))
                            || (!operations.empty() && operations.peek() == '-' && (prevPr >= getPriority(expr.charAt(i))) && expr.charAt(i)!='(')) {
                        while (!operations.empty() && operations.peek() != '(') {
                            result.append(operations.pop() + ",");
                        }
                    }

                    //inserting operations in stack according to operators priority
                    if((prevPr <= getPriority(expr.charAt(i)))) {
                        operations.push(expr.charAt(i));
                        prevPr = getPriority(expr.charAt(i));
                    } else {
                        /**
                         * if last operation's priority is gather than current's we have to get all the operations from stack and insert them to result string
                         */
                        while (!operations.empty() && operations.peek() != '(' && expr.charAt(i)!='(') {
                            result.append(operations.pop() + ",");
                        }
                        operations.push(expr.charAt(i));
                        prevPr = getPriority(expr.charAt(i));
                    }
                }
                prev='c';
            }
        }

        //extracting all the remaining operations
        while(!operations.empty()){
            result.append("," + operations.pop());
        }

        return result.toString().trim();
    }


    //Main method that checks expression, calls convert and compute methods
    public int calculate(String expr) throws IncorrectExpressionException, DividedByZero {
        expr = expr.replaceAll("\\s+", ""); //removing all spaces
        //Regular expression pattern to check if expression is entered correct
        Pattern correctPattern = Pattern.compile("/^\\d+$|^(\\(\\d+[\\+\\-\\*\\/]\\d+([\\+\\-\\*\\/]\\d+)*\\)[\\+\\-\\*\\/]?)*(\\d+([\\+\\-\\*\\/]\\d+)*([\\+\\-\\*\\/]\\(\\d+[\\+\\-\\*\\/]\\d+([\\+\\-\\*\\/]\\d+)*\\))*\\d*([\\+\\-\\*\\/]\\d+)*)*$"); //To use our regex we have to obtain it first (to compile into Pattern). This pattern will later be used in Mather
        Matcher matcher = correctPattern.matcher(expr);

        if(matcher.matches()) {
            if(expr.contains("/0")) //checking division on zero
                throw new DividedByZero();
            //if everything is good converting expression and calculating result...
            return compute(convert(expr));
        } else {
            throw new IncorrectExpressionException();
        }
    }

    //Methods that checks operation priority
    public int getPriority(Character op){
        switch(op){
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }
}
