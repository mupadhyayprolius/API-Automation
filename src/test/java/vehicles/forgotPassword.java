package vehicles;

import org.testng.Assert;
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

public class forgotPassword  {
	 String token = "";
	 String clientname ="rps";
	 String baseURI = "https://uat-"+clientname+"-api.fleetmastr.com";
	 String forgotpasswordIdvalid = "mupadhyay+rps1@aecordigital.com";
	 String forgotpasswordIdInvalid = "mupadhyay+rp@aecordigital.com";
		
	@Test (priority=1)
	public void forgotPasswordvalid()
	{
		RestAssured.baseURI = baseURI;
		JSONObject person = new JSONObject();
		person.put("email", forgotpasswordIdvalid);
		
		String jsonString = person.toJSONString();
	Response response = given()
.when()
.contentType(ContentType.JSON).body(jsonString)
.post("/api/v1/users/forgotPassword");
	response.prettyPrint();
	
assertEquals(response.getStatusCode(), 200, "Received a 200 response");


if (response.getStatusCode() == 200) {
    System.out.println("Api successfully run: " + response.getStatusCode());
       // Parse the response JSON to get the value of is_trailer_feature_enabled
    assertEquals(response.jsonPath().getString("message"), "An email has been sent to you with a password reset link");
}
else
{
    System.out.println("Api failed: " + response.getStatusCode());
}


}
	@Test (priority=2)
	public void forgotPasswordInvalid()
	{
		RestAssured.baseURI = baseURI;
		JSONObject person = new JSONObject();
		person.put("email", forgotpasswordIdInvalid);
		
		String jsonString = person.toJSONString();
	Response response = given()
.when()
.contentType(ContentType.JSON).body(jsonString)
.post("/api/v1/users/forgotPassword");
	response.prettyPrint();
	
assertNotEquals(response.getStatusCode(), 200, "Received a 200 response");
assertEquals(response.getStatusCode(), 400, "This email does not match our records.");

{
	 System.out.println("My response: " + response.statusCode() +  "\nMessage:  " + response.jsonPath().getString("message"));
		
		assertNotEquals(response.getStatusCode(), 200, "Received a 200 response");
		
			Assert.assertEquals(response.statusCode(), 400);


}
	}
	}