package assets;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import org.json.simple.JSONObject;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.util.HashMap;
import java.util.Map;

public class Configuration extends LoginAPI {

	String token = "";
	
	@Test (priority=2)
	public void ConfigurationAPI()
	
	{
RestAssured.baseURI = baseURI;
Response response = given()
.header("Authorization", "Bearer " + token) // Include the authorization token in the request header
.when()
.contentType(ContentType.JSON)
.get("/api/v1/configration");

assertEquals(response.getStatusCode(), 200, "Received a 200 response");


if (response.getStatusCode() == 200) {
    System.out.println("Api successfully run: " + response.getStatusCode());
    System.out.println(baseURI);
    // Parse the response JSON to get the value of is_trailer_feature_enabled
    JsonPath jsonPath = response.jsonPath();
    String isTrailerEnabled = jsonPath.getString("is_trailer_feature_enabled");
    System.out.println("Is trailer attached: " + isTrailerEnabled);
    if (isTrailerEnabled.equals("1")) {
        System.out.println("Asset is enabled");
    } else {
        System.out.println("Asset is disabled");
    }
      
        
} else {
    System.out.println("Api failed: " + response.getStatusCode());
}

response.prettyPrint();
}}