package APITEST;

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

public class Getallvehiclesdetails extends Getallvehicles {
	String odometer_reading;
	String defetList;
		
	 @Test  (priority=3)
	
	public void getallvehicledetails()
	{
	
		 RestAssured.baseURI = baseURI;		 
			JSONObject person = new JSONObject();
			person.put("action", "checkout");
			person.put("registration_no", registration_number);
			String jsonString = person.toJSONString();
Response response = given()
.header("Authorization", "Bearer " + token) // Include the authorization token in the request header
.when()
.contentType(ContentType.JSON)
.body(jsonString)
.post("api/v1/vehicle-details");
if (response.statusCode() == 200)
{
	
//	int dataSize = response.jsonPath().getList("data").size();
	
	
   
    	odometer_reading = response.jsonPath().getString("data.vehicle.odometer_reading");
    	defetList = response.asString();
        System.out.println("Odometer_Reading: " + odometer_reading);
        System.out.println("defetList : " + defetList);
       
        
   

}
else
{
	System.out.println("Api failed " + response.statusCode());

}


response.prettyPrint();
	
}
	 
}
