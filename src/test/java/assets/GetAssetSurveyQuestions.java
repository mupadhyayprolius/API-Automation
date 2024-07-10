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

public class GetAssetSurveyQuestions extends Getallassetsdetails{
	 String checkoutScreenJson = "";
	 String checkinScreenJson = "";
	 String defectScreenJson = "";
	 @Test  (priority=4)
	
	public void getvehiclesurveyquestions() throws org.json.simple.parser.ParseException, ParseException
	{
	
		 
		 System.out.println("GetVehiclesCheckHistory ");
		 RestAssured.baseURI = baseURI;		
		 
		 JSONObject person = new JSONObject();
			person.put("asset_id",asset_id);
			String jsonString = person.toJSONString();

Response response = given()
.header("Authorization", "Bearer " + token) // Include the authorization token in the request header
.when()
.contentType(ContentType.JSON)
.body(jsonString)
.post("/api/v1/survey/questions?"+asset_id);

if (response.statusCode() == 200) {
   // response.prettyPrint();
    String responseBody = response.getBody().asString();
    JSONParser parser = new JSONParser();
	JSONArray jsonArray = (JSONArray) parser.parse(responseBody);

	for (Object obj : jsonArray) {
	    JSONObject jsonObject = (JSONObject) obj;
	    String action = (String) jsonObject.get("action");
	    String screenJson = (String) jsonObject.get("screen_json");

	    if ("checkout".equals(action)) {
	    	checkoutScreenJson = screenJson;
	        System.out.println("screen_json for action 'checkout': " + checkoutScreenJson);
	    } if ("checkin".equals(action)) {
	         checkinScreenJson = screenJson;
	        System.out.println("screen_json for action 'checkin': " + checkinScreenJson);
	    }if ("defect".equals(action)) {
	    	defectScreenJson = screenJson;
	        System.out.println("screen_json for action 'defect': " + checkinScreenJson);
	    }
	}
} else {
    System.out.println("API failed " + response.statusCode());
}
}

public String getCheckoutScreenJson() {
return checkoutScreenJson;
}

public String getCheckinScreenJson() {
return checkinScreenJson;
}

public String getDefectScreenJson() {
	return defectScreenJson;
}
}