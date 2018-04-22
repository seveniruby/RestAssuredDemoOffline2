import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Base64;
import java.util.HashMap;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

public class TesterHomeTest {
    @BeforeClass
    public static void setup(){
        useRelaxedHTTPSValidation();
        RestAssured.baseURI="https://testerhome.com";
        RestAssured.proxy("127.0.0.1",8080);

        RestAssured.filters((req, res, ctx)->{
            if(req.getURI().contains("xueqiu.com")){
                req.header("testerhome_id", "seveniruby");
                req.cookie("device_id=864d4cb52ace61737d69da102e7e996d; __utmz=1.1516097049.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); s=fv11u1xhjc; Hm_lvt_1db88642e346389874251b5a1eded6e3=1523358353; __utmc=1; xq_a_token=0d524219cf0dd2d0a4d48f15e36f37ef9ebcbee1; xq_a_token.sig=P0rdE1K6FJmvC2XfH5vucrIHsnw; xq_r_token=7095ce0c820e0a53c304a6ead234a6c6eca38488; xq_r_token.sig=xBQzKLc4EP4eZvezKxqxXNtB7K0; u=491524279667126; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1524282101; __utma=1.1295158553.1516097049.1524279666.1524282101.14");
            }
            Response resOrigin=ctx.next(req, res);
            return resOrigin;
        });
    }

    @Test
    public void testHtml(){

        given()
                .queryParam("q", "appium")
        .when()
                .get("https://testerhome.com/search").prettyPeek()
        .then()
                .statusCode(200)
                .body("html.head.title", equalTo("appium · 搜索结果 · TesterHome"))
        ;

    }


    @Test
    public void testTesterHomeJson(){
        given().when().get("https://testerhome.com/api/v3/topics.json").prettyPeek()
        .then()
                .statusCode(200)
                .body("topics.title", hasItems("优质招聘汇总"))
                .body("topics.title[1]", equalTo("优质招聘汇总"))
                //.body("topics.id[-1]", equalTo(12986))
                .body("topics.findAll { topic->topic.id == 10254 }.title", hasItems("优质招聘汇总"))
                .body("topics.find { topic->topic.id == 10254 }.title", equalTo("优质招聘汇总"))
                //.body("**.find { topic->topic.id == 10254 }.title", equalTo("优质招聘汇总"))
                .body("topics.title.size()", equalTo(22))
                ;
    }

    @Test
    public void testTesterHomeJsonSingle(){
        given().when().get("https://testerhome.com/api/v3/topics/10254.json").prettyPeek()
        .then()
                .statusCode(200)
                .body("topic.title", equalTo("优质招聘汇总"))
                ;
    }
    @Test
    public void testTesterHomeSearch(){
        given().queryParam("q","霍格沃兹测试学院")
        .when().get("https://testerhome.com/search").prettyPeek()
        .then()
                .statusCode(200)
                ;
    }

    @Test
    public void testXML(){

        Response response=given().when().get("http://127.0.0.1:8000/hogwarts.xml").prettyPeek()
        .then()
                .statusCode(200)
                .body("shopping.category.item.name[2]", equalTo("Paper"))
                .body("shopping.category[1].item[1].name", equalTo("Pens"))
                .body("shopping.category.size()", equalTo(3))
                .body("shopping.category[1].item.size()", equalTo(2))
                .body("shopping.category.find { it.@type=='present' }.item.name", equalTo("Kathryn's Birthday"))
                .body("**.find { it.price == 200 }.name", equalTo("Kathryn's Birthday"))
        .extract().response()
        ;

        System.out.println(response.statusLine());
    }

    @Test
    public void testTesterHomeJsonSchema(){
        given().when().get("https://testerhome.com/api/v3/topics/6040.json").prettyPeek()
                .then()
                .statusCode(200).body(matchesJsonSchema(new File("/tmp/json2.schema")))
        ;
    }
    @Test
    public void testTesterHomeJsonGlobal(){

        given()
                .proxy("127.0.0.1", 8080)
        .when().get("/api/v3/topics/10254.json").prettyPeek()
        .then()
                .statusCode(200)
                .body("topic.title", equalTo("优质招聘汇总"))
        ;
    }

    @Test
    public void testJsonPost(){
        HashMap<String, Object> data=new HashMap<String, Object>();
        data.put("id", 6040);
        data.put("title", "通过代理安装 appium");
        data.put("name", "思寒");

        HashMap<String, Object> root=new HashMap<String, Object>();
        root.put("topic", data);

        given()
                .contentType(ContentType.JSON)
                .body(root)
        .when()
                .post("http://www.baidu.com").prettyPeek()
        .then()
                .time(lessThan(1500L))
                ;
    }

    @Test
    public void multiApi(){
        String name=given().get("https://testerhome.com/api/v3/topics/6040.json").prettyPeek()
        .then().statusCode(200).extract().path("topic.user.name")
        ;

        System.out.println(name);

        given()
                .queryParam("q", name)
                .cookie("uid", name)
        .when().get("/search")
        .then().statusCode(200).body(containsString(name))
                ;
    }

    @Test
    public void multiApiMultiData(){
        Response response=given().get("https://testerhome.com/api/v3/topics/6040.json").prettyPeek()
                .then().statusCode(200).extract().response()
                ;

        String name=response.path("topic.user.name");
        Integer uid=response.path("topic.user.id");

        System.out.println(name);
        System.out.println(uid);

        given()
                .queryParam("q", name)
                .cookie("name", name)
                .cookie("uid", uid)
        .when().get("/search")
        .then()
                .statusCode(200).body(containsString(name))
        ;
    }

    @Test
    public void testSpec(){
        ResponseSpecification rs=new ResponseSpecBuilder().build();
        rs.statusCode(200);
        rs.body(not(containsString("error")));
        rs.time(lessThan(5000L));

        given().get("/api/v3/topics/6040.json")
        .then().spec(rs);
    }


    @Test
    public void testFilter(){
        given().filter((requestSpecification, responseSpecification, ctx) -> {
            System.out.println(requestSpecification);
            Response res=ctx.next(requestSpecification, responseSpecification);
            ResponseBuilder rb=new ResponseBuilder().clone(res);
            rb.setBody("seveniruby");
            return rb.build();
        }).get("http://www.baidu.com").prettyPeek().then().statusCode(200);
    }


    @Test
    public void testSchema(){
        given().when().get("https://testerhome.com/api/v3/topics/6040.json")
        .then()
                .statusCode(200)
                .body(matchesJsonSchema(new File("/tmp/json.schema")))
        ;
    }

    @Test
    public void testSchemaInClassPath(){
        given().when().get("https://testerhome.com/api/v3/topics/6040.json")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schema/json.schema"))
        ;
    }

    @Test
    public void testFilterResponse(){
        given().log().all()
                .filter( (req, res, ctx)->{
                    //filter request
                    System.out.println(req.getURI());
                    req.header("a", "b");
                    //request real
                    Response resOrigin=ctx.next(req, res);
                    //response real
                    //filter response
                    System.out.println(resOrigin.body().asString());
                    String raw=new String(
                            Base64.getDecoder().decode(
                                    resOrigin.body().asString().trim()
                            )
                    );
                    ResponseBuilder resBuilder=new ResponseBuilder().clone(resOrigin);
                    resBuilder.setBody(raw);
                    //return new response;
                    return resBuilder.build();
                })
        .when()
                .get("http://127.0.0.1:8000/base64.json").prettyPeek()
        .then()
                .statusCode(200)
        ;
    }

    @Test
    public void testBase64(){
        String raw="eyJTT0dPIjp7InN5bWJvbCI6IlNPR08iLCJleGNoYW5nZSI6Ik5ZU0UiLCJjb2RlIjoiU09HTyIsIm5hbWUiOiLmkJzni5ciLCJjdXJyZW50IjoiOC40NyIsInBlcmNlbnRhZ2UiOiIwLjAiLCJjaGFuZ2UiOiIwLjAwIiwib3BlbiI6IjguNDIiLCJoaWdoIjoiOC40NyIsImxvdyI6IjguMjEiLCJjbG9zZSI6IjguNDciLCJsYXN0X2Nsb3NlIjoiOC40NyIsImhpZ2g1MndlZWsiOiIxNC43IiwibG93NTJ3ZWVrIjoiNy45MSIsInZvbHVtZSI6Ijc5NjAyMi4wIiwibG90X3ZvbHVtZSI6Ijc5NjAyMi4wIiwidm9sdW1lQXZlcmFnZSI6IjEzNTAyNDAiLCJtYXJrZXRDYXBpdGFsIjoiMy4zMTYxOTUyMDIzMkU5IiwiZXBzIjoiMC4yOSIsInBlX3R0bSI6IjI5LjIwNjkiLCJwZV9seXIiOiI0MC40NDE0IiwiYmV0YSI6IjAuMCIsInRvdGFsU2hhcmVzIjoiMzkxNTIyNDU2IiwidGltZSI6IkZyaSBBcHIgMjAgMTU6NTk6NTkgLTA0MDAgMjAxOCIsImFmdGVySG91cnMiOiI4LjI5IiwiYWZ0ZXJIb3Vyc1BjdCI6Ii0yLjEzIiwiYWZ0ZXJIb3Vyc0NoZyI6Ii0wLjE4IiwiYWZ0ZXJIb3Vyc1RpbWUiOiJGcmkgQXByIDIwIDIwOjAwOjA4IC0wNDAwIDIwMTgiLCJ1cGRhdGVBdCI6IjE1MjQyNTQ0NTAxODQiLCJkaXZpZGVuZCI6IjAuMCIsInlpZWxkIjoiMC4wIiwidHVybm92ZXJfcmF0ZSI6IjAuMjAlIiwiaW5zdE93biI6IjAuMCIsInJpc2Vfc3RvcCI6IjAuMCIsImZhbGxfc3RvcCI6IjAuMCIsImN1cnJlbmN5X3VuaXQiOiIiLCJhbW91bnQiOiI2NTgzMTAxLjk0IiwibmV0X2Fzc2V0cyI6IjAuMCIsImhhc2V4aXN0IjoiIiwiaGFzX3dhcnJhbnQiOiIwIiwidHlwZSI6IjEiLCJmbGFnIjoiMSIsInJlc3RfZGF5IjoiIiwiYW1wbGl0dWRlIjoiMi44MyUiLCJtYXJrZXRfc3RhdHVzIjoi5bey5pS255uYIiwibG90X3NpemUiOiIxIiwibWluX29yZGVyX3F1YW50aXR5IjoiMCIsIm1heF9vcmRlcl9xdWFudGl0eSI6IjAiLCJ0aWNrX3NpemUiOiIwLjAxIiwidmFyaWFibGVfdGlja19zaXplIjoiMC4wMDAxIDEgMC4wMSIsImt6el9zdG9ja19zeW1ib2wiOiIiLCJrenpfc3RvY2tfbmFtZSI6IiIsImt6el9zdG9ja19jdXJyZW50IjoiOC4yNyIsImt6el9jb252ZXJ0X3ByaWNlIjoiMC4wIiwia3p6X2NvdmVydF92YWx1ZSI6IjAuMCIsImt6el9jcHIiOiIwLjAiLCJrenpfcHV0YmFja19wcmljZSI6IjAuMCIsImt6el9jb252ZXJ0X3RpbWUiOiIiLCJrenpfcmVkZW1wdF9wcmljZSI6IjAuMCIsImt6el9zdHJhaWdodF9wcmljZSI6IjAuMCIsImt6el9zdG9ja19wZXJjZW50IjoiIiwicGIiOiIwLjAiLCJiZW5lZml0X2JlZm9yZV90YXgiOiIwLjAiLCJiZW5lZml0X2FmdGVyX3RheCI6IjAuMCIsImNvbnZlcnRfYm9uZF9yYXRpbyI6IiIsInRvdGFsaXNzdWVzY2FsZSI6IiIsIm91dHN0YW5kaW5nYW10IjoiIiwibWF0dXJpdHlkYXRlIjoiIiwicmVtYWluX3llYXIiOiIiLCJjb252ZXJ0cmF0ZSI6IiIsImludGVyZXN0cnRtZW1vIjoiIiwicmVsZWFzZV9kYXRlIjoiIiwiY2lyY3VsYXRpb24iOiIwLjAiLCJwYXJfdmFsdWUiOiIwLjAiLCJkdWVfdGltZSI6IjAuMCIsInZhbHVlX2RhdGUiOiIiLCJkdWVfZGF0ZSI6IiIsInB1Ymxpc2hlciI6IiIsInJlZGVlbV90eXBlIjoiVCIsImlzc3VlX3R5cGUiOiIiLCJib25kX3R5cGUiOiJUIiwid2FycmFudCI6IiIsInNhbGVfcnJnIjoiIiwicmF0ZSI6IiIsImFmdGVyX2hvdXJfdm9sIjoiNzk2MDIyIiwiZmxvYXRfc2hhcmVzIjoiMCIsImZsb2F0X21hcmtldF9jYXBpdGFsIjoiMC4wIiwiZGlzbmV4dF9wYXlfZGF0ZSI6IiIsImNvbnZlcnRfcmF0ZSI6IiIsInZvbHVtZV9yYXRpbyI6IiIsInBlcmNlbnQ1bSI6IjAuMCIsInBhbmtvdV9yYXRpbyI6Ijg4LjY4JSIsImRpdmlkZW5kX3BheV9kYXRlIjoiIiwiZXhfZGl2aWRlbmRfZGF0ZSI6IiIsIm1vdmluZ19hdmdfMjAwX2RheSI6IiIsImNoZ19mcm9tXzIwMF9kYXlfbW92aW5nX2F2ZyI6IiIsInBjdF9jaGdfZnJvbV8yMDBfZGF5X21vdmluZ19hdmciOiIiLCJtb3ZpbmdfYXZnXzUwX2RheSI6IiIsImNoZ19mcm9tXzUwX2RheV9tb3ZpbmdfYXZnIjoiIiwicGN0X2NoZ19mcm9tXzUwX2RheV9tb3ZpbmdfYXZnIjoiIiwic2hhcmVzX291dHN0YW5kaW5nIjoiIiwiZWJpdGRhIjoiIiwic2hvcnRfcmF0aW8iOiIiLCJwZV9lc3RpbWF0ZV9uZXh0X3llYXIiOiIiLCJwZWdfcmF0aW8iOiIiLCJlcHNfZXN0aW1hdGVfbmV4dF95ZWFyIjoiIiwiZXBzX2VzdGltYXRlX25leHRfcXVhcnRlciI6IiIsImVwc19lc3RpbWF0ZV9jdXJyZW50X3F1YXJ0ZXIiOiIiLCJwc3IiOiIiLCJyZXZlbnVlIjoiIiwicHJvZml0X21hcmdpbiI6IiJ9fQ==";
        System.out.println(new String(Base64.getDecoder().decode(raw.getBytes())));
    }

    @Test
    public void testFilterAddCooike(){
        given().log().all()
                .queryParam("code", "sogo")
        .when()
                .get("https://xueqiu.com/v4/stock/quote.json")
        .then()
                .log().all()
                .statusCode(200)

        ;
    }

    @Test
    public void testJsonInBase64(){
        given().filter( (req, res, ctx)->{
            Response resOrigin=ctx.next(req, res);
            ResponseBuilder responseBuilder=new ResponseBuilder().clone(resOrigin);
            //responseBuilder.setBody("{ \"SOGO\": { \"name\" : \"搜狗\"} }");
            String decodedContent=new String(
                    Base64.getDecoder().decode(
                            resOrigin.body().asString().trim()
                    )
            );
            responseBuilder.setBody(decodedContent);
            Response resNew=responseBuilder.build();
            return resNew;
        })
                //.get("https://xueqiu.com/v4/stock/quote.json?code=SOGO")
                .get("http://127.0.0.1:8000/base64.json").prettyPeek()
        .then()
                .statusCode(200)
                .body("SOGO.name", equalTo("搜狗"));
    }
    
}
