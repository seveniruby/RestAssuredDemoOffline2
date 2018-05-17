import io.restassured.RestAssured;
import io.restassured.config.RedirectConfig;
import io.restassured.config.SessionConfig;
import io.restassured.filter.session.SessionFilter;
import org.junit.Test;

import static io.restassured.RestAssured.*;


public class AuthTestDemo {
    @Test
    public void testHttpBase(){
        given()
                .auth().basic("hogwarts", "123456")
        .when()
                .get("http://101.132.159.87:9001/").prettyPeek()
        .then().statusCode(200);
    }

    @Test
    public void testJenkinsLogin(){
        RestAssured.config = RestAssured.config().sessionConfig(
                new SessionConfig().sessionIdName("JSESSIONID.86912bdc"));
        SessionFilter sessionFilter = new SessionFilter();

        given().log().all()
                .filter(sessionFilter)
                .queryParam("j_password", "testerhometesterhome")
                .queryParam("Submit", "%E7%99%BB%E5%BD%95")
                .queryParam("j_username", "testerhome")
                .when().post("http://47.100.105.183:8081/j_acegi_security_check").prettyPeek()
                .then()
                .statusCode(302)

        ;
        given().log().all().filter(sessionFilter)
        .when().get("http://47.100.105.183:8081/").prettyPeek()
        .then().statusCode(200);

    }

    @Test
    public void testAuth2(){

        given()
                .log().all()
                //.auth().oauth2("ca72af4d99e56f8b20323758687e18163aba38c9")
        .get("https://api.github.com/user/repos").prettyPeek();
    }
}
