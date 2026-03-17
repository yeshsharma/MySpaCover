package zasyasolutions.SpaCover.TestCases;

import io.restassured.RestAssured;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

public class ManualApiTestCase {
	
	
	@Test
	public void addPlace() {
		
		RestAssured.baseURI = ("https://www.rahulshettyacademy.com");
		
		Map<String, Object> payload = new HashMap<>();
		
		Map<String, Object> locations = new HashMap<>();
		locations.put("lat", -38.383494);
		locations.put("lng", 33.427362);
		payload.put("location",locations);
		
		payload.put("accuracy", 50);
		payload.put("name", "Frontline house");
		payload.put("phone_number", "(+91) 983 893 3937");
		payload.put("address", "29, side layout, cohen 09");
		List<String> types = new ArrayList<>(); 
		types.add("shoe park");
		types.add("shop");
		payload.put("types",types);
		payload.put("website", "http://google.com");
		payload.put("language", "French-IN");
		
		System.out.println(payload);
		
		
		
	}
	
	
	
	
	
	
	
	
	

}
