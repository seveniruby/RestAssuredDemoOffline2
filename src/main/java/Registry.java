import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Registry {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {"dubbo/registry.xml"});
        context.start();
        // press any key to exit
        System.in.read();
    }
}