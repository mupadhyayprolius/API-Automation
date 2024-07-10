package assets;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.awt.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

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

public class AssetReportDefect extends Getallassetsdetails{

    @Test(priority = 5)
    public void ReportAssetDefect() throws org.json.simple.parser.ParseException, ParseException {

        int apiID = new Random().nextInt(900000000) + 100000000;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        String json = getUserJson();
        
        System.out.print("String with ans : "+json);
        
        

        RestAssured.baseURI = baseURI;

        JSONObject requestBody = new JSONObject();
        requestBody.put("api-id", apiID);
        requestBody.put("action", "defect");
        requestBody.put("asset_id", asset_id);
        requestBody.put("status", "defectfree");
        requestBody.put("odometer_reading", odometer_readig.toString());
        requestBody.put("json", json);
        requestBody.put("location","22.3072,73.1812");
        requestBody.put("duration", getHourMinuteSecondFromMillisecond(System.currentTimeMillis()));
        requestBody.put("report_datetime", formattedDateTime);
        String jsonString = requestBody.toJSONString();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(jsonString)
                .post("/api/v1/asset/submit-check");
        response.then().log().all();
        assertEquals(response.getStatusCode(), 201, "Received a 201 response");
        if (response.statusCode() == 201) {
            response.prettyPrint();
        } else {
            System.out.println("\n\n AssetReportdefect API failed " + response.statusCode());
           response.prettyPrint();
        }
    }

    private String getUserJson() {
        String json = getDefectJson();

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(json);


            JSONArray screenArray = (JSONArray) jsonObject.get("screens");
        

            // Skip the 0th element
            for (int i = 0; i < screenArray.size(); i++) {
            	Boolean defectReported = false;
                JSONObject screenObj = (JSONObject) screenArray.get(i);
                JSONArray optionslist = (JSONArray) screenObj.get("defects_list");
                
                for(int j=0; j<optionslist.size(); j++)
                {
                	
                	 JSONObject opl = (JSONObject) optionslist.get(j);
                     JSONArray defectArray = (JSONArray) opl.get("defects");	
                     
                     for(int k=0;k<defectArray.size();k++) {
                    	 JSONObject defect = (JSONObject) defectArray.get(k);
                    	 if(defect.containsKey("has_image") && defect.get("has_image").equals("0") && defect.get("selected").equals("0")) {
                    		 System.out.println("Defect Added in : " +defect.get("title") );
                    		  defect.put("selected", "1"); 
                    		   defectReported = true;
                    		   break;
                    	 }
                     }
                     if(defectReported)
                    	 break;
                }
                if(defectReported)
               	 break;
               
            }
            return jsonObject.toJSONString();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


	public static String getHourMinuteSecondFromMillisecond(long millisecond) {
        int seconds = (int) ((millisecond / 1000) % 60);
        int minutes = (int) ((millisecond / (1000 * 60)) % 60);
        int hours = (int) ((millisecond / (1000 * 60 * 60)) % 24);
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
     
    }
}