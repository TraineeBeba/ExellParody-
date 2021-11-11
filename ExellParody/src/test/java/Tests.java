import com.exellParody.parserFiles.Evaluate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.HashSet;

class EvaluateTest {
    @Test
    public void unitTest1(){
        String test = "(2 * 5 ^ 3) mod 3";
        System.out.println(test);
        test = String.valueOf(Evaluate.evaluate(test));
        String answer = "1.0";
        System.out.println("Real answer: " + test + " Expected answer: " + answer);
        Assertions.assertEquals(answer, test);
    }
    @Test
    public void unitTest2(){
        String[][] formulas =new String[25][25];;
        formulas[2][2] = "#B1";
        formulas[0][1] = "0";
        HashSet<String> set = new HashSet<>();
        set.add("A1");
        String test = "max(min(max( 0, 3), min(10, 5)), #C3)";
        System.out.println(test);
        try {
            test = Evaluate.link(test, formulas);
            test = String.valueOf(Evaluate.evaluate(test));
            String answer = "3.0";
            System.out.println("Real answer: " + test + " Expected answer: " + answer);
            Assertions.assertEquals(answer, test);
        }
        catch (Exception ex){
            Assertions.assertEquals("1", "0");
        }
    }
    @Test
    public void unitTest3(){
        String test = "10 + b ^ 3";
        System.out.println(test);
        try {
            test = String.valueOf(Evaluate.evaluate(test));
        }
        catch (Exception ex){
            test = "ERROR";
        }
        String answer = "ERROR";
        System.out.println("Real answer: " + test + " Expected answer: " + answer);
        Assertions.assertEquals(answer, test);
    }
}