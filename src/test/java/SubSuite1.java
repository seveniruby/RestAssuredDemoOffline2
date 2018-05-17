import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SubSuite1 extends BaseSuite{
    @BeforeClass
    public static void setup(){
        System.out.println("SubSuite1 setup");
    }
    @Test
    public void test3(){

    }

    @Test
    public void test4(){

    }
}
