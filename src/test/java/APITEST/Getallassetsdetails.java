package APITEST;

import static io.restassured.RestAssured.given;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Getallassetsdetails extends Getallassets{
	 String checkoutScreenJson = "";
	 String checkinScreenJson = "";
	 String defectjson = "";
	 String odometer_readig = "";
	 String asset_id = "";
	 JSONArray defect_list;
	
	@Test (priority=3)
			public void getallasetsdetails() throws ParseException
	{
	
		RestAssured.baseURI = baseURI;
		
		 
		JSONObject person = new JSONObject();
		person.put("is_question_set_required", "TRUE");
		person.put("action", "takeout");
		person.put("serial_number", serialNumber);
		String jsonString = person.toJSONString();

		Response response = given().header("Authorization", "Bearer " + token)
				.when()
				.contentType(ContentType.JSON)
				.body(jsonString)
				.post("/api/v1/asset-details");
			response.then().log().all(); // This will print the request details
				Assert.assertEquals(response.statusCode(), 200, "Status code is not 200");
				if (response.statusCode() == 200)
				
				{
					String responseBody = response.getBody().asString();
					 JSONParser parser = new JSONParser();
			            JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
					
					
					JSONObject jsonObjectData = (JSONObject) jsonObject.get("data");
					defect_list = (JSONArray) jsonObjectData.get("preexisting_defects_list");
					System.out.println("defect_list : " + defect_list.toString());
					odometer_readig = (String) jsonObjectData.get("last_odometer_reading");
					asset_id =  jsonObjectData.get("id").toString();	
					 JSONArray screenArray = (JSONArray) jsonObjectData.get("jsons");
							
					for (Object obj : screenArray) {
					    JSONObject jsonObjectScreen = (JSONObject) obj;
					    String action = (String) jsonObjectScreen.get("action");
					    String screenJson = (String) jsonObjectScreen.get("json");
					    
					    if ("takeout".equals(action)) {
					    	checkoutScreenJson = screenJson;
					        System.out.println("screen_json for action 'takeout': " + checkoutScreenJson);
					    }
					    else if ("return".equals(action)) {
					         checkinScreenJson = screenJson;
					        System.out.println("screen_json for action 'return': " + checkinScreenJson);
					    }
					    else  if ("defect".equals(action)) {
					         defectjson = screenJson;
					        System.out.println("screen_json for action 'defect': " + defectjson);
					    }
					}
					System.out.println("Api successfully run"+ response.statusCode());

				}
				else
				{
					System.out.println("Api failed " + response.getBody().asString());

				}
					}
	

public String getCheckoutScreenJson() {
return checkoutScreenJson;
}

public String getCheckinScreenJson() {
return checkinScreenJson;
}

public String getDefectJson() {
	return defectjson;
	
}
				}