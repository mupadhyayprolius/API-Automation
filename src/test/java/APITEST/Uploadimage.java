package APITEST;

import static io.restassured.RestAssured.given;
import java.awt.List;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import bsh.ParseException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Random;
import java.io.IOException;
import java.io.InputStream;

public class Uploadimage extends Getallvehiclesdetails  {
	
	int apiID = 0;
		
	 @Test  (priority=4)
	
	public void uploadimage() throws org.json.simple.parser.ParseException, ParseException, IOException {

        apiID = new Random().nextInt(900000000) + 100000000;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:MM dd MMM yyyy");
        String formattedDateTime = now.format(formatter);
	{
		 String base64 = "data:image/png;base64,"+encodeImageToBase64("images/relax.jpg");
		 System.out.println(base64);
		 RestAssured.baseURI = baseURI;		 
			JSONObject person = new JSONObject();
			person.put("temp_id", apiID);
			person.put("stage", emailID);
			person.put("relates_to","");
			person.put("image_string", base64);
			person.put("image_exif", formattedDateTime+",37.785834;-122.406417");
			person.put("email", emailID);
			person.put("category", "defecthistory");
			String jsonString = person.toJSONString();
		 
			
			
Response response = given()
.header("Authorization", "Bearer " + token) // Include the authorization token in the request header
.when()
.contentType(ContentType.JSON)
.body(jsonString)
.post("api/v1/image/upload");

response.then().log().all();


if (response.statusCode() == 200)
{
	
	 System.out.println("image upload success");  
	 response.prettyPrint();
            }
else
{
	System.out.println("Api failed " + response.statusCode());
}}
}
	  public static String encodeImageToBase64(String imagePath) throws IOException {
		byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
		return Base64.getEncoder().encodeToString(imageBytes);

	}
}
