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

	  return DataLayerClient.fetchPostResponse(target, "application/json", goalJson);	
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

		return DataLayerClient.fetchPutResponse(target, "application/json", new JSONObject("{}"));
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
				.path(userid + "")
				.path("daily")
				.path(dueDate);		

		if (achieved.equals("true") || achieved.equals("false")) {
			target = target.queryParam("achieved", achieved);
		}

		return DataLayerClient.fetchGetResponse(target, "application/json");	
	}
}
