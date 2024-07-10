package vehicles;

import static io.restassured.RestAssured.given;

import java.awt.List;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Getallvehicles extends LoginAPI {

	int vehicle_id;
	String registration_number;
	 @Test  (priority=2)
	
	public void getallvehicles()
	{
	
	
RestAssured.baseURI = baseURI;
Response response = given()
.header("Authorization", "Bearer " + token) // Include the authorization token in the request header
.when()
.contentType(ContentType.JSON)
.post("/api/v2/vehicle/all");



if (response.statusCode() == 200)
{
	
	int dataSize = response.jsonPath().getList("data").size();
	
	Assert.assertTrue(dataSize > 0);
    if (dataSize > 0) {
        vehicle_id = response.jsonPath().getInt("data[0].vehicle_id");
        System.out.println("Vehicle ID: " + vehicle_id);
        registration_number = response.jsonPath().getString("data[0].registration_number");
        System.out.println("Registration_Number: " + registration_number);
    } else {
        System.out.println("No data in the response");
    }

}
else
{
	System.out.println("Api failed " + response.statusCode());

}
//response.prettyPrint();
	
	
}
}
