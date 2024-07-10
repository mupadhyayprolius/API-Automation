package assets;

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

public class test{

	 String token = "";
	 String clientname ="rps";
	 String baseURI = "https://uat-"+clientname+"-api.fleetmastr.com";
	 String emailID = "admin@imastr.com";
	@Test (priority=1)
	public void ConfigurationAPI()
	
	{
		RestAssured.baseURI = baseURI;
		JSONObject person = new JSONObject();
		person.put("email", "adm@imastr.com");
		person.put("password", "2Wsxm}#i");
		String jsonString = person.toJSONString();

		Response response = given().when().contentType(ContentType.JSON).body(jsonString).post("/api/v1/users/login");
		assertNotEquals(response.getStatusCode(), 200, "Received a 200 response");
		//Assert.assertEquals(response.statusCode(), 401, "Unauthorized");
		  if (response.getStatusCode() != 200) {
	       System.out.println("Error response:");
	       response.prettyPrint();
	       Assert.assertEquals(response.jsonPath().getString("message"), "These credentials do not match our records.");
	   } else {
	       System.out.println("Success response:");
	       response.prettyPrint();
		response.prettyPrint();
		}
}
}

