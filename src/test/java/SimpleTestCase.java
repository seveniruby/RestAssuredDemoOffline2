import com.xueqiu.service.com.xueqiu.service.login.LoginTestCase;
import org.junit.*;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SimpleTestCase extends LoginTestCase {
    @BeforeClass
    public static void beforeClass(){
        System.out.println("before class SimpleTestCase");
    }

    @Before
    public void before(){
        System.out.println("before");
    }

    @After
    public void after(){
        System.out.println("after");
    }
    @Test
    public void testB(){
        System.out.println("testB");

    }

    @Test
    public void test1(){
        System.out.println("test1");

    }
    @Test
    public void test2(){
        System.out.println("test2");

    }

    @Test
    public void testC(){
        System.out.println("testC");

    }

    @Test
    public void testA(){
        System.out.println("testA");

    }

    @AfterClass
    public static void afterClass(){
        System.out.println("after class SimpleTestCase");
    }
}
