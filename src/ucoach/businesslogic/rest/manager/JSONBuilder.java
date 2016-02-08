package ucoach.businesslogic.rest.manager;

import org.json.JSONObject;

public class JSONBuilder {
	
	/**
	 * 
	 * @param user
	 * @param body
	 * @param label
	 * @return
	 */
	public static JSONObject singleJsonResponse(JSONObject user, JSONObject body, String label){
		JSONObject json = new JSONObject();
		json.put("user", user);
		json.put(label, body);
		return json;
	}
	
	/**
	 * In case the label is not specified
	 * @param user
	 * @param body
	 * @return
	 */
	public static JSONObject singleJsonResponse(JSONObject user, JSONObject body){
		return singleJsonResponse(user, body, "body");
	}
	
	/**
	 * 
	 * @param user
	 * @param singleValue
	 * @param label
	 * @return
	 */
	public static JSONObject singleValueJsonResponse(JSONObject user, String singleValue, String label){
		JSONObject json = new JSONObject();
		json.put("user", user);
		json.put(label, singleValue);
		return json;
	}
}
