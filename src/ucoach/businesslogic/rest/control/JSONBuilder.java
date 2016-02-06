package ucoach.businesslogic.rest.control;

import org.json.JSONObject;

public class JSONBuilder {
	public static JSONObject singleJsonResponse(JSONObject user, JSONObject body, String label){
		JSONObject json = new JSONObject();
		json.put("user", user);
		json.put(label, body);
		return json;
	}
	
	//In case the label is not specified
	public static JSONObject singleJsonResponse(JSONObject user, JSONObject body){
		return singleJsonResponse(user, body, "body");
	}
	
	
}
