import org.example.calculator.Calculator;
import org.example.exceptions.DividedByZero;
import org.example.exceptions.IncorrectExpressionException;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorTest {

    private Calculator calculator;

    CalculatorTest(){
        calculator = new Calculator();
    }


    @ParameterizedTest
    @MethodSource("expressions")
    @DisplayName("Some simple tests")
    public void testCalculator(String expr, int expectedResult) throws DividedByZero, IncorrectExpressionException {
        assertEquals(expectedResult, calculator.calculate(expr));
    }


    public static Stream<Arguments> expressions(){
        return Stream.of(Arguments.arguments("11+11-(10-50)", 62),
                         Arguments.arguments("11+11-(12*45+5)+55-10", -478),
                         Arguments.arguments("11+11-(124/12)", 12),
                         Arguments.arguments("(11 + 18) * 20 - 2", 578),
                         Arguments.arguments("12+13*5-78+(1+23*4-56/2)", 64),
                         Arguments.arguments("12+13*5-78+(1+23*4-56/2)+25", 89),
                         Arguments.arguments("12+13*5-78+(1+23*4-56/2)+25-5", 84),
                         Arguments.arguments("12+13*5-78+(1+23*4-56/2)+25-5+6", 90),
                         Arguments.arguments("12+13*5-78+(1+23*4-56/2)+25-5+6*90-100", 524),
                         Arguments.arguments("12-1+13*5-78+(1+23*4-56/2)+25-5+6", 89),
                         Arguments.arguments("12-1-1+13*5-78+(1+23*4-56/2)+25-5+6", 88),
                         Arguments.arguments("12+13*5-78-(1+23*4-56/2)+25", -41),
                         Arguments.arguments("12-1-1+13*5-78+(1+23*4-56/2)+25-5+6", 88),
                         Arguments.arguments("12-1-1+1+13*5-78+(1+23*4-56/2)+25-5+6", 89),
                         Arguments.arguments("12-1-1+1+13*5-78+(1+23*4-56/2)*25-5+6", 1624),
                         Arguments.arguments("12-1-1+1+13*5-78+(1+23*4-56/2+2)+25-5+6", 91),
                         Arguments.arguments("10/(6-4)", 5),
                         Arguments.arguments("10/(6-4)/2", 2),
                         Arguments.arguments("10*(6-4)/2", 10));
    }


    @ParameterizedTest
    @DisplayName("Division By 0")

    @ValueSource(strings = {"1/0", "5/(1-1)"})

    public void devideByZero(String expr){
        assertThrows(DividedByZero.class, ()->calculator.calculate(expr));
    }


    @ParameterizedTest
    @ValueSource(strings = {"a+b*c", "1 plus 2"})
    @DisplayName("Incorrect expression")
    public void incorrectExpression(String expr){
        assertThrows(IncorrectExpressionException.class, ()->calculator.calculate(expr));
    }

//    @MethodSource("expressions") is used instead of this:
//    @ValueSource(strings = {"11+11-(10-50)",
//            "11+11-(12*45+5)+55-10",
//            "11+11-(124/12)",
//            "(11 + 18) * 20 - 2",
//            "12+13*5-78+(1+23*4-56/2)",
//            "12+13*5-78+(1+23*4-56/2)+25",
//            "12+13*5-78+(1+23*4-56/2)+25-5",
//            "12+13*5-78+(1+23*4-56/2)+25-5+6",
//            "12+13*5-78+(1+23*4-56/2)+25-5+6*90-100",
//            "12-1+13*5-78+(1+23*4-56/2)+25-5+6",
//            "12-1-1+13*5-78+(1+23*4-56/2)+25-5+6",
//            "12+13*5-78-(1+23*4-56/2)+25",
//            "12-1-1+13*5-78+(1+23*4-56/2)+25-5+6",
//            "12-1-1+1+13*5-78+(1+23*4-56/2)+25-5+6",
//            "12-1-1+1+13*5-78+(1+23*4-56/2)*25-5+6",
//            "12-1-1+1+13*5-78+(1+23*4-56/2+2)+25-5+6",
//            "10/(6-4)",
//            "10/(6-4)/2",
//            "10*(6-4)/2"})
}
