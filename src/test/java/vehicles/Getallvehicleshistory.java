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

public class Getallvehicleshistory extends Getallvehicles {
	int id;
	String type;
	String date;
	String report_datetime;
	String status;
	int total_defects;
	int preexisting_defects_count;
		
	 @Test  (priority=3)
	
	public void getallvehicleshistory()
	{
	
		 RestAssured.baseURI = baseURI;		 
			JSONObject person = new JSONObject();
			person.put("page", "1");
			person.put("vehicle_id", vehicle_id);
			String jsonString = person.toJSONString();
Response response = given()
.header("Authorization", "Bearer " + token) // Include the authorization token in the request header
.when()
.contentType(ContentType.JSON)
.body(jsonString)
.post("/api/v1/vehicle/history");
if (response.statusCode() == 200)
{
	
	int dataSize = response.jsonPath().getList("data").size();
	
	Assert.assertTrue(dataSize > 0);
    if (dataSize > 0) {
        id = response.jsonPath().getInt("data[0].id");
        type= response.jsonPath().getString("data[0].type");
        System.out.println("Type: " + type);
        System.out.println("Vehicle ID: " + id);
        date= response.jsonPath().getString("data[0].date");
        System.out.println("Date: " + date);
        report_datetime= response.jsonPath().getString("data[0].report_datetime");
        System.out.println("Report date time: " + report_datetime);
        status= response.jsonPath().getString("data[0].status");
        System.out.println("Vehicle Stauts: " + status);
        total_defects= response.jsonPath().getInt("data[0].total_defects");
        System.out.println("Total defects: " + total_defects);
        preexisting_defects_count= response.jsonPath().getInt("data[0].preexisting_defects_count");
        System.out.println("Preexisting Defects Count: " + preexisting_defects_count);     
        
    } else {
        System.out.println("No data in the response");
    }

}
else
{
	System.out.println("Api failed " + response.statusCode());

}


response.prettyPrint();
	
}
}
