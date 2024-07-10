package vehicles;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Loginapi_credentials {
	 String token = "";
	 String baseURI = "https://uat-skanska-api.fleetmastr.com";
	 @Test(priority = 1)
		public void LoginAPIwithvalidcredentials()
		{
		
	RestAssured.baseURI = baseURI;
	JSONObject person = new JSONObject();
	person.put("email", "admin@imastr.com");
	person.put("password", "aL4&9@qx");
	String jsonString = person.toJSONString();

	Response response = given().when().contentType(ContentType.JSON).body(jsonString).post("/api/v1/users/login");
	
	assertEquals(response.getStatusCode(), 200, "Received a 200 response");
	if (response.statusCode() == 200)
	{
		System.out.println("Api successfully run" + response.statusCode());
		token = response.jsonPath().getString("token");
		System.out.println("Login token" + token);

	}

	else
	{
		System.out.println("Api failed " + response.statusCode());

	}
	response.prettyPrint();
		}
	 @Test(priority = 2)
		public void LoginAPIwithinvalidcredentials()
		{
		
	RestAssured.baseURI = baseURI;
	JSONObject person = new JSONObject();
	person.put("email", "adm@imastr.com");
	person.put("password", "cor@2023");
	String jsonString = person.toJSONString();

	Response response = given().when().contentType(ContentType.JSON).body(jsonString).post("/api/v1/users/login");
	 System.out.println("My response" + response.statusCode() +  " error + " + response.jsonPath().getString("error"));
	
	assertNotEquals(response.getStatusCode(), 200, "Received a 200 response");
	
		Assert.assertEquals(response.statusCode(), 401 );
	
}
	 
	 @Test(priority = 3)
		public void Loginwithinvalidemail()
		{
		
	RestAssured.baseURI = baseURI;
	JSONObject person = new JSONObject();
	person.put("email", "adm@imastr.com");
	person.put("password", "Aecor@2023");
	String jsonString = person.toJSONString();

	Response response = given().when().contentType(ContentType.JSON).body(jsonString).post("/api/v1/users/login");
	 System.out.println("My response: " + response.statusCode() +  "\nerror:  " + response.jsonPath().getString("error"));
		
		assertNotEquals(response.getStatusCode(), 200, "Received a 200 response");
		
			Assert.assertEquals(response.statusCode(), 401 );
		
		
}
	 
}

