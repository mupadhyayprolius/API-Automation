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

public class Getvehicledefects extends Getallvehicles {

	String registration_number;
	 @Test  (priority=3)
	
	public void getvehicledefects()
	{
	
	
RestAssured.baseURI = baseURI;
JSONObject person = new JSONObject();
person.put("registration_no", registration_number);
String jsonString = person.toJSONString();

Response response = given().header("Authorization", "Bearer " + token)
		.when()
		.contentType(ContentType.JSON)
		.body(jsonString)
		.post("api/v1/vehicle/defect");



if (response.statusCode() == 200)
{
	
	int dataSize = response.jsonPath().getList("data").size();
	
	Assert.assertTrue(dataSize > 0);
    if (dataSize > 0) {
        vehicle_id = response.jsonPath().getInt("data[0].vehicle_id");
        System.out.println("Vehicle ID: " + vehicle_id);
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
