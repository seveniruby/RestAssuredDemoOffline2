import org.junit.BeforeClass;
import org.junit.Test;

public class BaseSuite {
    @BeforeClass
    public static void setupBase(){
        System.out.println("Base beforeClass");
    }
    @Test
    public void test1(){

    }

    @Test
    public void test2(){

    }
}
