package org.example.calculator;



import org.example.exceptions.DividedByZero;
import org.example.exceptions.IncorrectExpressionException;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private Stack<Character> operations;
    private Stack<Integer> numbers;

    public Calculator(){
        operations = new Stack<>();
        numbers = new Stack<>();
    }

    public int compute(String line) throws DividedByZero {
        List<String> elements = List.of(line.split(","));

        for (String e : elements) {
            if(e.matches("\\d+")){
                numbers.push(Integer.parseInt(e));
            } else {
                Integer n1;
                Integer n2;
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
//        elements.forEach((e)->{
//            if(e.matches("\\d+")){
//                numbers.push(Integer.parseInt(e));
//            } else {
//                Integer n1;
//                Integer n2;
//                switch(e){
//                    case "+":
//                        n1 = numbers.pop();
//                        n2 = numbers.pop();
//                        numbers.push(n2+n1);
//                        break;
//                    case "-":
//                        n1 = numbers.pop();
//                        n2 = numbers.pop();
//                        numbers.push(n2-n1);
//                        break;
//                    case "*":
//                        n1 = numbers.pop();
//                        n2 = numbers.pop();
//                        numbers.push(n2*n1);
//                        break;
//                    case "/":
//                        n1 = numbers.pop();
//                        n2 = numbers.pop();
//                        numbers.push(n2/n1);
//                        break;
//                }
//            }
//        });

        return numbers.pop();
    }

    public String convert(String expr){
        char prev = 'c';
        int prevPr=0;
        StringBuilder result = new StringBuilder();

        for(int i=0; i<expr.length(); i++){
            if(Character.isDigit(expr.charAt(i))){
                result.append(expr.charAt(i));
                prev='n';

            } else {
                if(expr.charAt(i)!='(' && expr.charAt(i)!=')')
                    result.append(",");


                if(expr.charAt(i)==')'){
                    char tmp = operations.pop();
                    while(tmp != '('){
                        result.append(","+tmp);
                        tmp=operations.pop();
                    }
//                    prevPr=3;
                    prevPr=0;

                } else {
                    //'-' operation is unique => if it is met or it is previous in stack we have to work it different, or if previous operation is -, or if previous operation is the same
                    if ((expr.charAt(i) == '-')
                            || (!operations.empty() && operations.peek() == expr.charAt(i))
                            || (!operations.empty() && operations.peek() == '-' && (prevPr >= getPriority(expr.charAt(i))) && expr.charAt(i)!='(')) {
                        while (!operations.empty() && operations.peek() != '(') {
                            result.append(operations.pop() + ",");
                        }
                    }

//                    if (expr.charAt(i) != '(') {
                        if ((prevPr <= getPriority(expr.charAt(i)))) {
                            operations.push(expr.charAt(i));
                            prevPr = getPriority(expr.charAt(i));
                            //                } else if(prevPr==3){
                            //                    result.append(operations.pop() + ",");
                            //                    operations.push(expr.charAt(i));
                            //                    prevPr = getPriority(expr.charAt(i));
                        } else {
                            while (!operations.empty() && operations.peek() != '(' && expr.charAt(i)!='(') {
                                result.append(operations.pop() + ",");
                            }
                            operations.push(expr.charAt(i));
                            prevPr = getPriority(expr.charAt(i));
                        }
//                    }
                }
//                }
                prev='c';
            }
        }

        while(!operations.empty()){
            result.append("," + operations.pop());
        }

        return result.toString().trim();
    }


    public int calculate(String expr) throws IncorrectExpressionException, DividedByZero {
        expr = expr.replaceAll("\\s+", "");
        Pattern correctPattern = Pattern.compile("/^\\d+$|^(\\(\\d+[\\+\\-\\*\\/]\\d+([\\+\\-\\*\\/]\\d+)*\\)[\\+\\-\\*\\/]?)*(\\d+([\\+\\-\\*\\/]\\d+)*([\\+\\-\\*\\/]\\(\\d+[\\+\\-\\*\\/]\\d+([\\+\\-\\*\\/]\\d+)*\\))*\\d*([\\+\\-\\*\\/]\\d+)*)*$"); //To use our regex we have to obtain it first (to compile into Pattern). This pattern will later be used in Mather
        Matcher matcher = correctPattern.matcher(expr);

//        System.out.println(expr);

        if(matcher.matches()) {
            if(expr.contains("/0"))
                throw new DividedByZero();

            return compute(convert(expr));
        } else {
            throw new IncorrectExpressionException();
        }
    }

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

/*
11+11-(10-50)
11+11-(12*45+5)+55-10
11+11-(124/12)
(11 + 18) * 20 - 2
12+13*5-78+(1+23*4-56/2)
12+13*5-78+(1+23*4-56/2)+25
12+13*5-78+(1+23*4-56/2)+25-5
12+13*5-78+(1+23*4-56/2)+25-5+6
12+13*5-78+(1+23*4-56/2)+25-5+6*90-100
12-1+13*5-78+(1+23*4-56/2)+25-5+6
12-1-1+13*5-78+(1+23*4-56/2)+25-5+6
12+13*5-78-(1+23*4-56/2)+25
12-1-1+13*5-78+(1+23*4-56/2)+25-5+6
12-1-1+1+13*5-78+(1+23*4-56/2)+25-5+6
12-1-1+1+13*5-78+(1+23*4-56/2)*25-5+6
12-1-1+1+13*5-78+(1+23*4-56/2+2)+25-5+6
10/(6-4)
10/(6-4)/2
10*(6-4)/2
*/

//todo: Clean the code!
