package APITEST;
import static io.restassured.RestAssured.given;
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

public class VehicleReportDefect extends GetVehicleSurveyQuestions{

    @Test(priority = 5)
    public void VehicleReportDefect() throws org.json.simple.parser.ParseException, ParseException {

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
        requestBody.put("vehicle_id", vehicle_id);
        requestBody.put("user_id", user_id);
        requestBody.put("odometer_reading", odometer_reading.toString());
        requestBody.put("json", json);
        requestBody.put("location","22.3072,73.1812");
        requestBody.put("check_duration", getHourMinuteSecondFromMillisecond(System.currentTimeMillis()));
        requestBody.put("report_datetime", formattedDateTime);
        String jsonString = requestBody.toJSONString();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(jsonString)
                .post("/api/v1/check/submit-check");
        response.then().log().all();
        if (response.statusCode() == 200) {
            response.prettyPrint();
        } else {
            System.out.println("\n\n VehicleReportDefect API failed " + response.statusCode());
           response.prettyPrint();
        }
    }

    private String getUserJson() {
        String json = getDefectScreenJson();

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(json);


            JSONObject screensObject = (JSONObject) jsonObject.get("screens");
            JSONArray screenArray = (JSONArray) screensObject.get("screen");
        

            // Skip the 0th element
            for (int i = 0; i < screenArray.size(); i++) {
            	Boolean defectReported = false;
                JSONObject screenObj = (JSONObject) screenArray.get(i);
                JSONObject options = (JSONObject) screenObj.get("options");
                JSONArray optionslist = (JSONArray) options.get("optionList");
                
                for(int j=0; j<optionslist.size(); j++)
                {
                	
                	 JSONObject opl = (JSONObject) optionslist.get(j);
                	 JSONObject defects = (JSONObject) opl.get("defects");
                     JSONArray defectArray = (JSONArray) defects.get("defect");	
                     
                     for(int k=0;k<defectArray.size();k++) {
                    	 JSONObject defect = (JSONObject) defectArray.get(k);
                    	 if(defect.containsKey("_image") && defect.get("_image").equals("no") && defect.get("selected").equals("no")) {
                    		 System.out.println("Defect Added in : " +defect.get("text") );
                    		  defect.put("selected", "yes"); 
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
            jsonObject.put("status","SafeToOperate");
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