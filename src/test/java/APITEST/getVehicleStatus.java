package APITEST;

import static io.restassured.RestAssured.given;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class getVehicleStatus extends Getallvehicles{

	@Test  (priority=3)
	public void getvehiclestatus()
	{
	
	
RestAssured.baseURI = baseURI;
//JSONObject person = new JSONObject();
//person.put("id","1");
//String jsonString = person.toJSONString();

Response response = given()
.header("Authorization", "Bearer " + token)
.when()
.contentType(ContentType.JSON)
.post("/api/v1/get_vehicle_status/"+vehicle_id);

response.then()
.log().all(); // This will print the request details


Assert.assertEquals(response.statusCode(), 200, "Status code is not 200");


if (response.statusCode() == 200)
{
	System.out.println("Api successfully run"+ response.statusCode());

}

else
{
	System.out.println("Api failed " + response.getBody().asString());

}
	}
	
	
	

}
