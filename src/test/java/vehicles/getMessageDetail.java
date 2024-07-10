package vehicles;

import static io.restassured.RestAssured.given;

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

public class getMessageDetail extends Messages {

	@Test(priority = 3)

	public void getMessageDetail() {

		RestAssured.baseURI = baseURI;
		JSONObject person = new JSONObject();
		person.put("id", message_id);
		String jsonString = person.toJSONString();
		Response response = given().header("Authorization", "Bearer " + token) // Include the authorization token in the
																				// request header
				.when().contentType(ContentType.JSON).body(jsonString).post("/api/v1/message/text");
		if (response.statusCode() == 200) {
			System.out.println("Api passed " + response.statusCode());
			response.prettyPrint();

		} else {
			System.out.println("Api failed " + response.statusCode());

		}

		response.prettyPrint();

	}

}
