package APITEST;

import static io.restassured.RestAssured.given;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.Test;

import bsh.ParseException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class SyncApi extends GetVehicleSurveyQuestions {

	String assetSerialNumber = "";
	String assetCheckoutScreenJson = "";
	String assetOdometer_readig = "";
	String assetassetID = "";

	@Test(priority = 5)
	public void getallasets() {

		System.out.println("Get Asset All Api Calling ");

		RestAssured.baseURI = baseURI;

		Response response = given().header("Authorization", "Bearer " + token) // Include the authorization token in the
																				// request header
				.when().contentType(ContentType.JSON).post("/api/v1/assets/all");
		if (response.statusCode() == 200) {
			int dataSize = response.jsonPath().getList("data.assets").size();

			Assert.assertTrue(dataSize > 0);
			if (dataSize > 0) {
				assetSerialNumber = response.jsonPath().getString("data.assets[0].serial_number");
				System.out.println("serial Number: " + assetSerialNumber);
			} else {
				System.out.println("No data in the response");
			}

		} else {
			System.out.println("Api failed " + response.statusCode());

		}

	}

	@Test(priority = 6)
	public void getallasetsdetails() throws ParseException, org.json.simple.parser.ParseException {

		System.out.println("Get Asset Detail Api Calling ");
		RestAssured.baseURI = baseURI;

		JSONObject person = new JSONObject();
		person.put("is_question_set_required", "TRUE");
		person.put("action", "takeout");
		person.put("serial_number", assetSerialNumber);
		String jsonString = person.toJSONString();

		Response response = given().header("Authorization", "Bearer " + token).when().contentType(ContentType.JSON)
				.body(jsonString).post("/api/v1/asset-details");
		response.then().log().all(); // This will print the request details
		Assert.assertEquals(response.statusCode(), 200, "Status code is not 200");
		if (response.statusCode() == 200)

		{
			String responseBody = response.getBody().asString();
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(responseBody);

			JSONObject jsonObjectData = (JSONObject) jsonObject.get("data");
			assetOdometer_readig = (String) jsonObjectData.get("last_odometer_reading");
			assetassetID = jsonObjectData.get("id").toString();
			JSONArray screenArray = (JSONArray) jsonObjectData.get("jsons");

			for (Object obj : screenArray) {
				JSONObject jsonObjectScreen = (JSONObject) obj;
				String action = (String) jsonObjectScreen.get("action");
				String screenJson = (String) jsonObjectScreen.get("json");

				if ("takeout".equals(action)) {
					assetCheckoutScreenJson = screenJson;
					System.out.println("screen_json for action 'takeout': " + checkoutScreenJson);
				}

			}
			System.out.println("Api successfully run" + response.statusCode());

		} else {
			System.out.println("Api failed " + response.getBody().asString());

		}
	}

	public String getAssetCheckoutScreenJson() {
		return assetCheckoutScreenJson;
	}

// Call Sync Api From Here

	@Test(priority = 7)
	public void callSyncApi() {

		System.out.println("Sync Data Api Calling ");

		int apiID = new Random().nextInt(900000000) + 100000000;

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDateTime = now.format(formatter);

		// Vehicle Check
		System.out.println("Deep 1");
		String vehicleJson = getVeicleJson();
		JSONObject vehicleData = new JSONObject();

		JSONObject requestVehicleBody = new JSONObject();
		requestVehicleBody.put("action", "checkout");
		requestVehicleBody.put("vehicle_id", vehicle_id);
		requestVehicleBody.put("user_id", user_id);
		requestVehicleBody.put("odometer_reading", odometer_reading.toString());
		requestVehicleBody.put("json", vehicleJson);
		requestVehicleBody.put("location", vehicle_id);
		requestVehicleBody.put("check_duration", getHourMinuteSecondFromMillisecond(System.currentTimeMillis()));
		requestVehicleBody.put("report_datetime", formattedDateTime);

		vehicleData.put("action", "check/submit-check");
		vehicleData.put("payload", requestVehicleBody.toString());

		// Asset Check
		System.out.println("Deep 2");
		String assetJson = getAssetJson();

		JSONObject assetData = new JSONObject();
		JSONObject requestAssetBody = new JSONObject();
		requestAssetBody.put("action", "takeout");
		requestAssetBody.put("asset_id", assetassetID);
		requestAssetBody.put("location", "40.7128, -74.0060");
		requestAssetBody.put("report_datetime", formattedDateTime);
		requestAssetBody.put("duration", getHourMinuteSecondFromMillisecond(System.currentTimeMillis()));
		requestAssetBody.put("status", "safetooperate");
		requestAssetBody.put("json", assetJson);
		requestAssetBody.put("is_same_check_for_return", "0");
		requestAssetBody.put("odometer_reading", assetOdometer_readig.toString());
		assetData.put("action", "asset/submit-check");
		assetData.put("payload", requestAssetBody.toString());
		System.out.println("Deep3");

		JSONArray jsonArray = new JSONArray();
		jsonArray.add(vehicleData);
		jsonArray.add(assetData);

		String jsonDataString = jsonArray.toString();
		System.out.println("Deep 4 :"+jsonArray);
		
		System.out.println("Deep 4 ----:"+jsonDataString.length());

		JSONObject syncData = new JSONObject();
		syncData.put("data", jsonDataString);
		System.out.flush();
		String jsonString = syncData.toString();
		System.out.println("Deep 5");
		System.out.println("API Sync Json " + jsonString);
		System.out.println("API Sync Json " + jsonString.length());

		Response response = given().header("Authorization", "Bearer " + token).header("api-id", apiID)
				.contentType(ContentType.JSON).body(jsonString).post("/api/v1/sync");

		response.then().log().all();

		if (response.statusCode() == 200) {
			response.prettyPrint();
		} else {
			System.out.println("API failed " + response.statusCode());
		}

	}

	private String getAssetJson() {
		String json = getAssetCheckoutScreenJson();
		System.out.println("my json:" + json);
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(json);

			// JSONObject screensObject = (JSONObject) jsonObject.get("screens");
			JSONArray screenArray = (JSONArray) jsonObject.get("screens");

			// Skip the 0th element
			for (int i = 1; i < screenArray.size(); i++) {
				JSONObject screenObj = (JSONObject) screenArray.get(i);
				screenObj.put("answer", "yes");
			}
			System.out.println("Asset Ans json:" + jsonObject.toString());
			return jsonObject.toString();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String getVeicleJson() {
		String json = getCheckoutScreenJson();

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
				} else {
					// Add answer key with yes option
					screenObj.put("answer", "yes");
				}
			}
			
			jsonObject.put("status","SafeToOperate");
			System.out.println("vehicle Ans json:" + jsonObject.toString());
			return jsonObject.toString();
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
