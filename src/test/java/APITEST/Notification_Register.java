package APITEST;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import org.json.simple.JSONObject;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.util.HashMap;
import java.util.Map;

public class Notification_Register extends LoginAPI {

	String token = "";
	
	@Test (priority=2)
	public void notificationRegistration()
	
	{
		
		RestAssured.baseURI = baseURI;
		JSONObject person = new JSONObject();
		person.put("email", emailID);
		person.put("registration_id","cpRsA1g2G0gOlHlg2T4eYF");
		String jsonString = person.toJSONString();
Response response = given()
.header("Authorization", "Bearer " + token) // Include the authorization token in the request header
.when()
.contentType(ContentType.JSON).body(jsonString)
.post("/api/v1/notification/register");

assertEquals(response.getStatusCode(), 200, "Received a 200 response");



}}