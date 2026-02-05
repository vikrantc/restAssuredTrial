import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;


import static io.restassured.RestAssured.given;
//import org.junit.jupiter.api.Test;


public class RestAssuredAPITest {

//    @Test
//    public void testGetBooksDetails() {
//        RestAssured.baseURI = "https://api.restful-api.dev/objects";
////        String accessToken = given()
////                .contentType("application/x-www-form-urlencoded")
////                .formParam("grant_type", "password")
////                .formParam("username", "test")
////                .formParam("password", "test")
//////                .formParam("client_id", "your_client_id")
//////                .formParam("client_secret", "your_client_secret")
////                .when()
////                .post("http://localhost:8000/auth/token")
////                .then()
////                .statusCode(200)
////                .extract().path("access_token");
////
////        System.out.println("access token: " + accessToken);
//
////        Response response= given().auth().oauth2(accessToken).when().get("/user");
////        System.out.println(response.getBody().asString());
//    }

    @Test
    public void testGetAllObjects(){
        RestAssured.baseURI = "https://api.restful-api.dev";
        Response response=given().when().get("/objects");
        System.out.println(response.statusCode());
//        System.out.println(response.asString());
        Assertions.assertEquals(200, response.statusCode());
        System.out.println(    response.jsonPath().getList("id"));
        Device[] devices = response.as(Device[].class);
        for (Device device : devices) {
            System.out.println(device.getId()+ " " + device.getName()+ " " + device.getData());
        }
    }

    @Test
    public void testGetObjectByIdQueryParam(){
        List<String> ids = new ArrayList<>(Arrays.asList("1","3","7"));
        RestAssured.baseURI = "https://api.restful-api.dev";
        Response response=given().queryParam("id",1).queryParam("id",3).queryParam("id",7).when().get("/objects");
        Assertions.assertTrue(response.jsonPath().getList("id").equals(ids));
    }

    @Test
    public void testGetObjectByIdPathParam(){
        RestAssured.baseURI = "https://api.restful-api.dev";
        Response response=given().pathParam("id","1").when().get("/objects/{id}");
        Assertions.assertTrue(response.jsonPath().get("id").equals("1"));
    }

    @Test
    public void testAddObject(){
        RestAssured.baseURI = "https://api.restful-api.dev";
        Device newDevice = new Device("","Apple MacBook Pro 16",new HashMap<>(Map.of("year","2019","price","1849.99","CPU model","Intel Core i9","Hard disk size","1 TB")));
        Response response=given().contentType("application/json").body(newDevice).post("/objects");
        Assertions.assertTrue(response.statusCode()==200);
        Assertions.assertTrue(response.jsonPath().get("name").equals("Apple MacBook Pro 16"));
        response=given().pathParam("id",response.jsonPath().get("id")).when().get("/objects/{id}");
        Assertions.assertTrue(response.jsonPath().get("name").equals("Apple MacBook Pro 16"));
    }
}