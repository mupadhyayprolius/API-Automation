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

public class VehicleResolveDefect extends Uploadimage{

    @Test(priority = 5)
    public void VehicleResolveDefect() throws org.json.simple.parser.ParseException, ParseException {


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        String defectId = getDefectId(defetList);
        
        

        RestAssured.baseURI = baseURI;

        JSONObject requestBody = new JSONObject();
        requestBody.put("defect_id",defectId);
        requestBody.put("defect_temp_id", "");
        requestBody.put("engineer_first_name", "Manjil");
        requestBody.put("engineer_last_name", "Automation");
        requestBody.put("engineer_id", "123");
        requestBody.put("selected_workshop", "other");
        requestBody.put("other_workshop","Abc");
        requestBody.put("reference_number","Dsads");
        requestBody.put("additional_information","additionalInformation");
        requestBody.put("status", "SafeToOperate");
        requestBody.put("report_datetime",formattedDateTime);
        JSONArray imagesArray = new JSONArray();
        imagesArray.add(apiID);
        requestBody.put("job_details_images",imagesArray);
        requestBody.put("additional_image_array",new JSONArray());
        String jsonString = requestBody.toJSONString();

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(jsonString)
                .post("/api/v1/resolve_defect");
        
        String jsonStringReq = requestBody.toJSONString();

        System.out.println("Request Body: " + jsonStringReq);
        
        response.then().log().all();

        if (response.statusCode() == 200) {
            response.prettyPrint();
        } else {
            System.out.println("\n\n VehicleReportDefect API failed " + response.statusCode());
           response.prettyPrint();
        }
    }

    private String getDefectId(String json) {
        String defectId = "";

        try {
            org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
            org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) parser.parse(json);

            org.json.simple.JSONArray defectsList = (org.json.simple.JSONArray) ((org.json.simple.JSONObject) jsonObject.get("meta")).get("defects_list");

            for (Object defectsObj : defectsList) {
                org.json.simple.JSONObject defectObj = (org.json.simple.JSONObject) defectsObj;
                org.json.simple.JSONArray addedDefects = (org.json.simple.JSONArray) defectObj.get("added_defects");

                if (addedDefects.size() > 0) {
                    org.json.simple.JSONObject defectDetails = (org.json.simple.JSONObject) addedDefects.get(0);
                    defectId = defectDetails.get("defect_id").toString();
                    System.out.println("defectId :  " + defectId);
                    break;
                }
            }

            return defectId;
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            return ""; // or throw new RuntimeException("Error parsing JSON", e);
        }
    }

}