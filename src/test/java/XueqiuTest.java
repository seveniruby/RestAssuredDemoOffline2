import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
public class XueqiuTest {
    public static String code;
    public static RequestSpecification requestSpecification;

    public static ResponseSpecification responseSpecification;
    @BeforeClass
    public static void Login(){
        useRelaxedHTTPSValidation();
        //RestAssured.proxy("127.0.0.1", 7778);
        //RestAssured.baseURI="https://api.xueqiu.com";


        requestSpecification= new RequestSpecBuilder().build();
        requestSpecification.port(80);
        requestSpecification.cookie("testerhome_id", "hogwarts");
        requestSpecification.header("User-Agent", "XueqiuTest Android 10.2");

        responseSpecification=new ResponseSpecBuilder().build();
        responseSpecification.statusCode(200);
        responseSpecification.body("code", equalTo(1));
        //loginXueqiu();


    }
    public static  void loginXueqiu(){
        code=given()
                .header("User-Agent", "XueqiuTest Android 10.2")
                .queryParam("_t", "1GENYMOTIONed534efb6ff44bbdae1e9192253f9a1a.2087663213.1523770568272.1523772396164")
                .queryParam("_s", "cdf894")
                .cookie("u", "2087663213")
                .cookie("xq_a_token","2c1b28b8a33f1d4f0474feaa11afd9200952cbd6")
                .formParam("grant_type", "password")
                .formParam("telephone", "15600534760")
                .formParam("password", "e10adc3949ba59abbe56e057f20f883e")
                .formParam("areacode", "86")
                .formParam("captcha", "")
                .formParam("client_id", "JtXbaMn7eP")
                .formParam("client_secret", "txsDfr9FphRSPov5oQou74")
                //.formParam("sid", "1GENYMOTIONed534efb6ff44bbdae1e9192253f9a1a")
                .when().post("/provider/oauth/token")
                .then()
                .log().all()
                .statusCode(400)
                //.body("error_code", equalTo("20082"))
                .extract().path("error_code");

        System.out.println(code);
    }
    @Test
    public void testSearch(){
        //信任https的任何证书
        useRelaxedHTTPSValidation();

        //given开头表示输入数据
        Response response=given().log().all()
            //query请求
            .queryParam("code", "sogo")
            //头信息
            .header("Cookie", "device_id=864d4cb52ace61737d69da102e7e996d; __utmz=1.1516097049.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); s=fv11u1xhjc; xq_a_token=229a3a53d49b5d0078125899e528279b0e54b5fe; xq_a_token.sig=oI-FfEMvVYbAuj7Ho7Z9mPjGjjI; xq_r_token=8a43eb9046efe1c0a8437476082dc9aac6db2626; xq_r_token.sig=Efl_JMfn071_BmxcpNvmjMmUP40; Hm_lvt_1db88642e346389874251b5a1eded6e3=1523358353; u=851523358353984; __utmc=1; __utma=1.1295158553.1516097049.1523673254.1523696123.10; __utmt=1; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1523696133; __utmb=1.3.10.1523696123")
        //表示触发条件
        .when()
            .get("https://xueqiu.com/stock/search.json")
        //对结果断言
        .then()
            .log().all()
            //状态码断言
            .statusCode(200)
            //字段断言
            .body("stocks.name", hasItems("搜狗"))
            .body("stocks.code",hasItems("SOGO"))
            .body("stocks.find {it.code ==  'SOGO' }.name", equalTo("搜狗"))
        .extract().response()
        ;

    }

    @Test
    public void testLogin(){


        given()
                .queryParam("sort","relevance")
                .queryParam("count", 5)
                .queryParam("page", 1)
                .queryParam("q", "sog")
                .cookie("u", "2087663213")
                .cookie("xq_a_token", "2c1b28b8a33f1d4f0474feaa11afd9200952cbd6")
                .cookie("uid", code)
        .when().get("https://api.xueqiu.com/integrate/smart_search.json")
        .then().log().all()
                .statusCode(200)
                .body("stock_list.find { it.name == '搜狗' }.code", equalTo("SOGO"))
                ;
    }


    @Test
    public void testLogin2(){
        useRelaxedHTTPSValidation();
        Response response=given().proxy("127.0.0.1", 7778)
                .queryParam("_t", "1GENYMOTIONed534efb6ff44bbdae1e9192253f9a1a.2087663213.1523770568272.1523772396164")
                .queryParam("_s", "cdf894")
                .cookie("u", "2087663213")
                .cookie("xq_a_token","2c1b28b8a33f1d4f0474feaa11afd9200952cbd6")
                .formParam("grant_type", "password")
                .formParam("telephone", "15600534760")
                .formParam("password", "e10adc3949ba59abbe56e057f20f883e")
                .formParam("areacode", "86")
                .formParam("captcha", "")
                .formParam("client_id", "JtXbaMn7eP")
                .formParam("client_secret", "txsDfr9FphRSPov5oQou74")
                //.formParam("sid", "1GENYMOTIONed534efb6ff44bbdae1e9192253f9a1a")
                .when().post("https://api.xueqiu.com/provider/oauth/token")
                .then()
                .log().all()
                .statusCode(400)
                //.body("error_code", equalTo("20082"))
                .extract().response();

        System.out.println(response);
    }

    @Test
    public void testPostJson(){
        HashMap<String, Object> map= new HashMap<String, Object>();
        map.put("a", 1);
        map.put("b", "testerhome");
        map.put("array", new String[] {"111", "2222"});
        given()
                .spec(requestSpecification)
                .contentType(ContentType.JSON)
                .body(map)
                //.body("{\"a\":1,\"b\":\"testerhome\",\"array\":[\"111\",\"2222\"]}")
        .when().post("http://www.baidu.com")
        .then()
                .log().all().time(lessThan(1000L)).body("code", equalTo("ErrorDemo"));
    }
}
