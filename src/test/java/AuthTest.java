import io.restassured.RestAssured;
import io.restassured.config.SessionConfig;
import io.restassured.filter.session.SessionFilter;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.*;

public class AuthTest {
    @BeforeClass
    public static void setupClass(){
        RestAssured.proxy("127.0.0.1", 8080);
    }
    @Test
    public void testHttpBaseAuth(){
        given()
                .auth().basic("hogwarts", "123456")
        .when()
                .get("http://101.132.159.87:9002/baidu.html").prettyPeek()
        .then()
                .statusCode(200);
    }

    @Test
    public void testJenkinsLogin(){

        RestAssured.config=RestAssured.config().sessionConfig(
                new SessionConfig().sessionIdName("JSESSIONID.86912bdc"));

        SessionFilter sessionFilter=new SessionFilter();

        given()
                .filter(sessionFilter)
                .queryParam("Submit", "%E7%99%BB%E5%BD%95")
                .queryParam("j_password", "testerhometesterhome")
                .queryParam("j_username", "testerhome")
                .queryParam("from", "%2F")
        .when()
                .log().all()
                .post("http://47.100.105.183:8081/j_acegi_security_check")
                .prettyPeek()
        .then()
                .log().all()
                .statusCode(302)
        ;

        given()
                .filter(sessionFilter)
        .when().log().all()
                .get("http://47.100.105.183:8081/").prettyPeek()
        .then()
                .statusCode(200);
    }

    @Test
    public void testAuth2(){
        useRelaxedHTTPSValidation();

        String token="407fe2c75e2fcd7d65fa5ce136e185da0858f5d4";
        given()
                .auth().oauth2(token)
        .when()
                .get("https://api.github.com/user")
                .prettyPeek()
        .then()
                .statusCode(200);

    }
}
