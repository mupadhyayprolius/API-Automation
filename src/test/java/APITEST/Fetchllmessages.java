package APITEST;

import static io.restassured.RestAssured.given;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Fetchllmessages extends LoginAPI{
	String serialNumber;
	String trailerID;
 
	@Test (priority=2)
			public void getalltrailers()
	{
	
	
RestAssured.baseURI = baseURI;
Response response = given()
.header("Authorization", "Bearer " + token) // Include the authorization token in the request header
.when()
.contentType(ContentType.JSON)
.post("/api/v1/trailers/all");
if (response.statusCode() == 200)
{
	int dataSize = response.jsonPath().getList("data.trailers").size();

	Assert.assertTrue(dataSize > 0);
	if (dataSize > 0) {
		serialNumber = response.jsonPath().getString("data.trailers[0].serial_number");
		trailerID = response.jsonPath().getString("data.trailers[0].id");
	    System.out.println("serial Number: " + serialNumber);
	    System.out.println("id: " + trailerID);
	} else {
	    System.out.println("No data in the response");
	}


}
else
{
	System.out.println("Api failed " + response.statusCode());

}

	
}
}