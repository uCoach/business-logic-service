package ucoach.datalayer.restclient;

import java.util.Date;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import ucoach.datalayer.restclient.util.DataLayerClient;
import ucoach.util.DatePatterns;
import ucoach.util.JsonParser;

public class GoalDataClient {

	static JsonParser jsonParser = new JsonParser();
	
	/*Register a new goal*/
	public static Response registerGoal(JSONObject goalJson) throws Exception{
		WebTarget baseTarget = DataLayerClient.getWebTarget();
		WebTarget target = baseTarget
				.path("goal");
		try{
			return DataLayerClient.fetchPostResponse(target, "application/json", goalJson);
		}catch(Exception ex){
			return Response.status(500).build();
		}		
	}
	
	/*Register a new goal changing the Dates*/
	public static Response RegisterAndChangeDate(JSONObject goalJson, Date created, Date due) throws Exception {
		WebTarget baseTarget = DataLayerClient.getWebTarget();
		WebTarget target = baseTarget
				.path("goal");
		
		goalJson.put("createdDate", DatePatterns.dateFormater(created));
		goalJson.put("dueDate", DatePatterns.dateFormater(due));
		try{
			return DataLayerClient.fetchPostResponse(target, "application/json", goalJson);
		}catch(Exception ex){
			return Response.status(500).build();
		}		
	}
	
	/*
	 * This function makes a request for registering a goal as achieved
	 * Makes a put request sending the JSONOBject empty
	 * Returns the result
	 */
	public static Response setGoalAsAchieved(int goalId){
		WebTarget baseTarget = DataLayerClient.getWebTarget();
		WebTarget target = baseTarget
				.path("goal")
				.path(goalId+"")
				.path("achieved");
		try{
			return DataLayerClient.fetchPutResponse(target, "application/json", new JSONObject("{}"));
		}catch(Exception ex){
			return Response.status(500).build();
		}
	}
	
	/*
	 * GEt the goals from a user from one certain due date
	 * achieved can assume the values of "true" and "false"
	 * */	
	public static Response getGoalsFromUser(int userid, Date from, String achieved) {

		WebTarget baseTarget = DataLayerClient.getWebTarget();
		WebTarget target = baseTarget
				.path("goal")
				.path("user")
				.path(userid+"");

		if (from != null){
			target = target.queryParam("dueDateFrom", DatePatterns.dateFormater(from));
		}

		if (achieved.equals("true") || achieved.equals("false")) {
			target = target.queryParam("achieved", achieved);
		}	

		return DataLayerClient.fetchGetResponse(target, "application/json");
	}
	
	/*
	 * Returns the DailyGoals from a certain date
	 * achieved can assume true or false or null
	 * 
	 * */
	public static Response getDailyGoalsFromUser(int userid, Date from, String achieved) {

		WebTarget baseTarget = DataLayerClient.getWebTarget();
		String dueDate = DatePatterns.dateFormater(from);
		WebTarget target = baseTarget
				.path("goal")
				.path("user")
				.path(userid+"")
				.path("daily")
				.path(dueDate);		
		if(achieved != null){
			if (achieved != "true" && achieved!="false")
				return Response.status(400).build();
			target = target.queryParam("achieved", achieved);
		}		
		try{
			return DataLayerClient.fetchGetResponse(target, "application/json");
		}catch(Exception ex){
			return Response.status(500).build();
		}		
	}
}
