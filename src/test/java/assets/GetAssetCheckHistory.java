package assets;
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

public class GetAssetCheckHistory extends Getallassetsdetails {
		
	 @Test  (priority=4)
	
	public void GetAssetCheckHistory() throws org.json.simple.parser.ParseException, ParseException
	{
	
		 
		 System.out.println("GetAssetCheckHistory ");
		 RestAssured.baseURI = baseURI;		
		 System.out.println("asset_id" + asset_id);
		 JSONObject person = new JSONObject();
			person.put("asset_id", asset_id);
			person.put("page", 1);
			String jsonString = person.toJSONString();

Response response = given()
.header("Authorization", "Bearer " + token) // Include the authorization token in the request header
.when()
.contentType(ContentType.JSON)
.body(jsonString)
.post("api/v1/asset/history");
response.then().log().all();
if (response.statusCode() == 200) {
   response.prettyPrint();
    
} else {
    System.out.println("API failed " + response.statusCode());
}
}
}