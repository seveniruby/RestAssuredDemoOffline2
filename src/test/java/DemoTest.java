import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class DemoTest {
    @Test
    public void testJson(){
        when().get("http://127.0.0.1:8000/testerhome.json")
        .then()
            .body("store.book.category", hasItems("reference"))
            .body("store.book[0].author", equalTo("Nigel Rees"))
            .body("store.book.find { book -> book.price == 8.95f }.price", equalTo(8.95f))
           // .body("**.findAll { it.price == 8.95f }.price", equalTo(8.95f))
        ;
            //.body("**.lottoId", equalTo(5));
            //.body("*.book.find { it.winnerId == 23 }", equalTo(23));
    }

    @Test
    public void testXML(){
        when().get("http://192.168.31.99:8000/testerhome.xml")
        .then()
            .body("shopping.category.item[0].name", equalTo("Chocolate"))
            .body("shopping.category.item.size()", equalTo(5))
            .body("shopping.category.findAll { it.@type == 'groceries' }.size()",equalTo(1))
            .body("shopping.category.item.findAll { it.price == 20 }.name",equalTo("Coffee"))
            .body("**.findAll { it.price == 20 }.name",equalTo("Coffee"))
        ;

    }
}
