import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import static org.junit.Assert.*;
public class DubboTest {
    public static DemoService demoService;
    @BeforeClass
    public static void setup(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"dubbo/consumer.xml"});
        context.start();
        demoService = (DemoService) context.getBean("demoService");

    }
    @Test
    public void testHelloWorld(){
        assertEquals(demoService.sayHello(""), "Hello ");
        assertEquals(demoService.sayHello("World"), "Hello World");

    }
    @Test
    public void testHelloSeveniruby(){
        assertEquals(demoService.sayHello("seveniruby"), "Hello Seveniruby");

    }
}
