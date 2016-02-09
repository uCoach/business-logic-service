package ucoach.businesslogic.rest.manager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import ucoach.datalayer.restclient.GoalDataClient;
import ucoach.datalayer.restclient.HealthMeasureDataClient;
import ucoach.util.DatePatterns;
import ucoach.util.JsonParser;

 /*
 * There are two kinds of goal, seasonal and ordinary
 */

 /*
 * Recurrent: That has to be performed in every day/week/month/year, ex: 7000 steps per day
 * From the creating date until the due date it will have ordinary goals according to the time
 * */
 /*
 * Fixed: That has to be performed in between dates, ex: have 65 Kg until 1st of March
 * Every seasonal goal generates one unique goal 
 */

public class GoalManager {

	static JsonParser jsonParser = new JsonParser();
	int userId;
	
	/**
	 * Constructor
	 * @param userId
	 */
	public GoalManager(int userId){
		this.userId = userId;
	}
	
	/**
	 * Update goal list
	 * @param jsonGoals
	 * @return
	 * @throws Exception
	 */
	public JSONArray updateGoals(JSONArray jsonGoals) {
		
		//perform the JSON Update goal for all elements of the JSONArray
		for (int i = 0; i < jsonGoals.length(); i++) {
			try {
				JSONObject goal = updateGoal(jsonGoals.getJSONObject(i));
				jsonGoals.put(i, goal);
			} catch (Exception e) {
				continue;
			}
		}
		return jsonGoals;
	}
	
	/**
	 * Update goal
	 * @param goal
	 * @return
	 * @throws Exception
	 */
	public JSONObject updateGoal(JSONObject goal) throws Exception {							
		//Initialize the variables
		Date createdDate = DatePatterns.dateParser(goal.getString("createdDate"));
		Date dueDate = DatePatterns.dateParser(goal.getString("dueDate"));
		int user = userId;
		int goalId = goal.getInt("id");

		JSONObject hmTypeJson = goal.getJSONObject("hmType");
		int hmType = hmTypeJson.getInt("id");

		//getting the objective (>,<,>=,...)
		String objective = goal.getString("objective");
		
		//getting the actual goal value
		float goalValue = getFloat(goal, "value");
		float reachedValue = getReachedValue(hmType, user,  createdDate,  dueDate);

		//Verifying the objective, if so, update the goal to achieved
		if (verifyObjective(objective, reachedValue, goalValue)) {
			goal.put("achieved", 1);				

			// Persist to database
			GoalDataClient.setGoalAsAchieved(goalId);
		}
		
		return goal;		
	}

	/**
	 * Helper method to parse goal and get value
	 * @param goal
	 * @return
	 * @throws Exception
	 */
	private static float getFloat(JSONObject goal, String expr) throws Exception {
		JsonParser parser = new JsonParser();
		parser.loadJson(goal.toString());
		
		String value = parser.getElement(expr);
		return Float.parseFloat(value);
	}

	/**
	 * 
	 * @param hmTypeId
	 * @param user
	 * @param startDate
	 * @param dueDate
	 * @return
	 * @throws Exception 
	 */
	public static float getReachedValue(int hmTypeId, int user, Date startDate, Date dueDate) throws Exception {		
		/*
		 * HERE COMES THE GET FOR THE hmTypeName
		 * '1', 'weight', 'kg'
		 * '2', 'height', 'cm'
		 * '3', 'steps', 'steps'
		 * '4', 'heart rate', 'bpm'
		 * '5', 'calories', 'calories'
		 * '6', 'running', 'km'
		 * '7', 'walking', 'km'
		 * '8', 'cycling', 'km'
		 * '9', 'sleeping', 'hours'
		 * 
		 */

		switch (hmTypeId){
			case 1: //reach a weight x
				return lastMeasure(hmTypeId,user, startDate, dueDate);
			case 2: //reach a height x
				return lastMeasure(hmTypeId,user, startDate, dueDate);
			case 3: //walk x steps
				return sumMeasures(hmTypeId,user, startDate, dueDate);
			case 4: //reach a heart rate
				return lastMeasure(hmTypeId,user, startDate, dueDate);
			case 5: //spend x calories
				return sumMeasures(hmTypeId,user, startDate, dueDate);
			case 6: //run x Kilometers
				return sumMeasures(hmTypeId,user, startDate, dueDate);
			case 7: //walk x kilometers
				return sumMeasures(hmTypeId,user, startDate, dueDate);
			case 8: //cycle x kilometers
				return sumMeasures(hmTypeId,user, startDate, dueDate);
			case 9: //sleep x hours
				return sumMeasures(hmTypeId,user, startDate, dueDate);				
		}
		
		throw new Exception("Measure type " + hmTypeId + " not supported");
	}
	
	/**
	 * 
	 * @param hmTypeId
	 * @param user
	 * @param startDate
	 * @param dueDate
	 * @return
	 * @throws Exception  
	 */
	public static float sumMeasures(int hmTypeId, int user, Date startDate, Date dueDate) throws Exception {
		//take the HealthMeasures from the hmType
		Response res = HealthMeasureDataClient.getHealthMeasures(user, hmTypeId, startDate, dueDate);
		if (res.getStatus() != 200 && res.getStatus() != 202) throw new Exception();

		//JSONObject healthMeasuresJSON = HealthMeasureDataClient.getHealthMeasures(user, hmTypeId, startDate, dueDate);
		JSONArray measures = new JSONArray(res.readEntity(String.class));

		if (measures.length() == 0) throw new Exception();

		float reachedValue = 0;
		for(int j = 0; j < measures.length(); j++){
			JSONObject measure = measures.getJSONObject(j);
			
			reachedValue += getFloat(measure, "value");				
		}

		return reachedValue;
	}
	
	/**
	 * 
	 * @param hmTypeId
	 * @param user
	 * @param startDate
	 * @param dueDate
	 * @return
	 * @throws Exception 
	 */
	public static float lastMeasure(int hmTypeId, int user, Date startDate, Date dueDate) throws Exception {
		//take the HealthMeasures from the hmType
		Response res = HealthMeasureDataClient.getHealthMeasures(user, hmTypeId, startDate, dueDate);
		if (res.getStatus() != 200 && res.getStatus() != 202) throw new Exception();
		
		JSONArray measures = new JSONArray(res.readEntity(String.class));
		
		if (measures.length() == 0) throw new Exception();

		JSONObject measure = measures.getJSONObject(measures.length() - 1);		
		return getFloat(measure, "value");
	}

	/**
	 * Verify if the first value attend to the objective when compared to the second
	 * @param objective
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static boolean verifyObjective(String objective, float v1, float v2){
		switch (objective) {
			case ">":
				return (v1 > v2);				
			case ">=":
				return (v1 >= v2);
			case "<":
				return(v1 < v2);
			case "<=":
				return(v1 <= v2);
			case "=":
				return (v1 == v2);
			default:
				return false;
		}
	}
	
	/**
	 * 
	 * @param jsonGoals
	 * @return
	 * @throws Exception
	 */
	public static JSONArray cloneDailyGoals(JSONArray jsonGoals, int userId) throws Exception {
		
		JSONArray newGoals = new JSONArray();

		//perform the JSON Update goal for all elements of the JSONArray
		for (int i = 0; i < jsonGoals.length(); i++) {
			try {
				JSONObject goal = cloneDailyGoal(jsonGoals.getJSONObject(i), userId);
				newGoals.put(i, goal);

			} catch (Exception e) {
				System.out.println("EXCEPTION: " + e.getMessage());
				continue;
			}
		}

		return newGoals;
	}
	
	/**
	 * 
	 * @param goal
	 * @return
	 * @throws Exception
	 */
	public static JSONObject cloneDailyGoal(JSONObject goal, int userId) throws Exception {

		Date today = new Date();
		JSONObject newGoal = new JSONObject();
		JsonParser parser = new JsonParser();
		parser.loadJson(goal.toString());

		newGoal.put("frequency", "daily");
		newGoal.put("createdDate", DatePatterns.dateFormater(today));
		newGoal.put("dueDate", DatePatterns.dateFormater(today));
		
		String hmTypeId = parser.getElement("hmType/id");
		String objective = parser.getElement("objective");
		String value = parser.getElement("value");
		newGoal.put("measureType", hmTypeId);
		newGoal.put("objective", objective);
		newGoal.put("value", value);
		newGoal.put("userId", Integer.toString(userId));

		Response res = GoalDataClient.registerGoal(newGoal);
		if (res.getStatus() != 200 && res.getStatus() != 202 && res.getStatus() != 201)
			throw new Exception("Response returned: " + res.readEntity(String.class));

		return new JSONObject(res.readEntity(String.class));
	}
}
