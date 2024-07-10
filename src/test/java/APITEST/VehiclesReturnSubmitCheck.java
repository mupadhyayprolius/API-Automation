package APITEST;

import static io.restassured.RestAssured.given;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.Test;

import bsh.ParseException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class VehiclesReturnSubmitCheck extends GetVehicleSurveyQuestions {

    @Test(priority = 5)
    public void submitVehiclesReturnSubmitCheck() throws org.json.simple.parser.ParseException, ParseException {

        int apiID = new Random().nextInt(900000000) + 100000000;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        String json = getUserJson();
        
        System.out.print("String with ans : "+json);

        RestAssured.baseURI = baseURI;

        JSONObject requestBody = new JSONObject();
        requestBody.put("api-id", apiID);
        requestBody.put("action", "checkin");
        requestBody.put("vehicle_id", vehicle_id);
        requestBody.put("user_id", user_id);
        requestBody.put("odometer_reading", vehicle_id);
        requestBody.put("json", json);
        requestBody.put("location", vehicle_id);
        requestBody.put("check_duration", getHourMinuteSecondFromMillisecond(System.currentTimeMillis()));
        requestBody.put("report_datetime", formattedDateTime);
        String jsonString = requestBody.toJSONString();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(jsonString)
                .post("/api/v1/check/submit-check");

        if (response.statusCode() == 200) {
            response.prettyPrint();
        } else {
            System.out.println("API failed " + response.statusCode());
        }
    }

    private String getUserJson() {
        String json = getCheckinScreenJson();

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(json);


            JSONObject screensObject = (JSONObject) jsonObject.get("screens");
            JSONArray screenArray = (JSONArray) screensObject.get("screen");
        

            // Skip the 0th element
            for (int i = 1; i < screenArray.size(); i++) {
                JSONObject screenObj = (JSONObject) screenArray.get(i);
                if ("Trailer Check".equals(screenObj.get("title"))) {
                    // Add answer key with no value
                    screenObj.put("answer", "no");
                } 
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
