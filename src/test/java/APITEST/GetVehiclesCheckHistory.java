package APITEST;
import static io.restassured.RestAssured.given;
import java.awt.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.Test;

import bsh.ParseException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class GetVehiclesCheckHistory extends Getallvehicleshistory {
		
	 @Test  (priority=4)
	
	public void getVehiclesCheckHistory() throws org.json.simple.parser.ParseException, ParseException
	{
	
		 
		 System.out.println("GetVehiclesCheckHistory ");
		 RestAssured.baseURI = baseURI;		
		 
		 JSONObject person = new JSONObject();
			person.put("id",vehicle_id);
			String jsonString = person.toJSONString();

Response response = given()
.header("Authorization", "Bearer " + token) // Include the authorization token in the request header
.when()
.contentType(ContentType.JSON)
.body(jsonString)
.post("/api/v1/check");

if (response.statusCode() == 200) {
   response.prettyPrint();
    
} else {
    System.out.println("API failed " + response.statusCode());
}
}
}