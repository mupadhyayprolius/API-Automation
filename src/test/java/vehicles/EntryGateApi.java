package vehicles;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class EntryGateApi {
	 String token = "";
	 String clientname ="cunbarpaints";
	 String baseURI = "https://api.uat-fleetmastr-entry-gate.fleetmastr.com";
	 String emailID = "spatel+cubbar@aecordigital.com";
		@Test (priority=1)
		public void postCofiguration() throws SignatureException
		{
		
	RestAssured.baseURI = baseURI;
	JSONObject person = new JSONObject();
	person.put("email", emailID);
	
	String jsonString = person.toJSONString();

	Response response = given()
			.header("X-Signature",hashMac(emailID))
			.when().contentType(ContentType.JSON).body(jsonString).post("/api/get_user_details");
	
	response.then().log().all();
	assertEquals(response.getStatusCode(), 200, "Received a 200 response");
	if (response.statusCode() == 200)
	{
		response.prettyPrint();
	}

	else
	{
		System.out.println("Api failed " + response.statusCode());

	}
	//
		}
		private static final String HASH_ALGORITHM = "HmacSHA256";
		
		public static String hashMac(String text) throws SignatureException {
	        try {
	            JSONObject json = new JSONObject();
	            json.put("email", text);
	            SecretKeySpec sk = new SecretKeySpec(getSecretKey().getBytes(), HASH_ALGORITHM);
	            Mac mac = Mac.getInstance(sk.getAlgorithm());
	            mac.init(sk);
	            byte[] hmac = mac.doFinal(json.toString().getBytes());
	            return toHexString(hmac);
	        } catch (NoSuchAlgorithmException e1) {
	            throw new SignatureException("error building signature, no such algorithm in device " + HASH_ALGORITHM);
	        } catch (InvalidKeyException e) {
	            throw new SignatureException("error building signature, invalid key " + HASH_ALGORITHM);
	        }
	    }
		
		private static String getSecretKey() {
	        // Replace this with your secret key logic
	        return "WCgnOdMtUoNUaSNzVtubYsRSrGQ0tE4WGJi9sFfgCog=";
	    }
		
		private static String toHexString(byte[] bytes) {
	        StringBuilder sb = new StringBuilder();
	        for (byte b : bytes) {
	            sb.append(String.format("%02x", b));
	        }
	        return sb.toString();
	    }
		
}
