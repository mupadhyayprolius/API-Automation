package vehicles;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.awt.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Messages extends LoginAPI {
	String message_id = "";

	@Test(priority = 2)

	public void getAllMessageList() {

		RestAssured.baseURI = baseURI;
		JSONObject person = new JSONObject();
		person.put("id", emailID);
		String jsonString = person.toJSONString();
		Response response = given().header("Authorization", "Bearer " + token) // Include the authorization token in the
																				// request header
				.when().contentType(ContentType.JSON).body(jsonString).post("/api/v1/message/fetchAll");
		assertEquals(response.getStatusCode(), 200, "Received a 200 response");
		if (response.statusCode() == 200) {
			System.out.println("Api passed " + response.statusCode());
			message_id = response.jsonPath().getString("message_id").replace("[", "").replace("]", "");

			System.out.println("MessageID :" + message_id);

		} else {
			System.out.println("Api failed " + response.statusCode());

		}

		response.prettyPrint();

	}

	 @Test  (priority=3)
		
		public void getMessageDetail()
		{
		
			 RestAssured.baseURI = baseURI;		 
				JSONObject person = new JSONObject();
				person.put("id", message_id);
				String jsonString = person.toJSONString();
	Response response = given()
	.header("Authorization", "Bearer " + token) // Include the authorization token in the request header
	.when()
	.contentType(ContentType.JSON)
	.body(jsonString)
	.post("api/v1/message/text");
	assertEquals(response.getStatusCode(), 200, "Received a 200 response");
	response.then().log().all();
	if (response.statusCode() == 200)
	{
		System.out.println("Api passed " + response.statusCode());

	}
	else
	{
		System.out.println("Api failed " + response.statusCode());

	}


	response.prettyPrint();
		
	}
	 
	 @Test  (priority=4)
		
		public void acknowledgeMessage()
	{

		RestAssured.baseURI = baseURI;
		JSONObject person = new JSONObject();
		person.put("message_id", message_id);
		person.put("email", emailID);
		person.put("status", "delivered");
		String jsonString = person.toJSONString();
		Response response = given().header("Authorization", "Bearer " + token) // Include the authorization token in the
																				// request header
				.when().contentType(ContentType.JSON).body(jsonString).post("api/v1/push_message/acknowledge");
		assertEquals(response.getStatusCode(), 200, "Received a 200 response");
		if (response.statusCode() == 200) {
			System.out.println("Api passed " + response.statusCode());

		} else {
			System.out.println("Api failed " + response.statusCode());

		}

		response.prettyPrint();

	}
}
