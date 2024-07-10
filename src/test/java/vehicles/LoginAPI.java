package vehicles;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class LoginAPI {
	 String token = "";
	 String clientname ="icl";
	 String baseURI = "https://uat-"+clientname+"-api.fleetmastr.com";
	 String emailID = "mupadhyay+icl@aecordigital.com";
	 String forgotpasswordId = "admin"+clientname+"@aecordigital.com";
	 String user_id;
		@Test (priority=1)
		public void postCofiguration()
		{
		
	RestAssured.baseURI = baseURI;
	JSONObject person = new JSONObject();
	person.put("email", emailID);
	person.put("password", "2Wsxm}#i");
	String jsonString = person.toJSONString();

	Response response = given().when().contentType(ContentType.JSON).body(jsonString).post("/api/v1/users/login");
	assertEquals(response.getStatusCode(), 200, "Received a 200 response");
	if (response.statusCode() == 200)
	{
		System.out.println("Api successfully run" + response.statusCode());
		token = response.jsonPath().getString("token");
		System.out.println("Login token" + token);
		user_id = response.jsonPath().getString("data.id");
	       
        System.out.println("User_Id: " + user_id);
	}

	else
	{
		System.out.println("Api failed " + response.statusCode());

	}
	//response.prettyPrint();
		}
		
}
