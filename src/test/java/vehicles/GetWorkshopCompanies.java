package vehicles;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GetWorkshopCompanies extends LoginAPI{
 
	@Test (priority=2)
			public void getallasets()
	{
	
	
RestAssured.baseURI = baseURI;
/*Map<String, String> bodydata = new HashMap<String, String>();
bodydata.put("email", "admin@imastr.com");
bodydata.put("password", "Aecor@2023");
bodydata.put("last_logout_state", "1");*/

/*JSONObject person = new JSONObject();
person.put("email", "admin@imastr.com");
person.put("password", "Aecor@2023");
String jsonString = person.toJSONString();*/

Response response = given()
.header("Authorization", "Bearer " + token) // Include the authorization token in the request header
.when()
.contentType(ContentType.JSON)
.get("api/v1/get_workshop_companies");
assertEquals(response.getStatusCode(), 200, "Received a 200 response");
if (response.statusCode() == 200)
{
	System.out.println("Api successfully run" + response.statusCode());

}

else
{
	System.out.println("Api failed " + response.statusCode());

}
response.prettyPrint();
	
	
}
}
