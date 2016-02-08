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
	public GoalManager(int userId){
		this.userId = userId;
	}
	public JSONArray updateGoals(JSONArray jsonGoals) throws Exception{
		//perform the JSON Update goal for all elements of the JSONArray
		for(int i = 0; i<jsonGoals.length(); i++){
			JSONObject goal = jsonGoals.getJSONObject(i);
			goal = updateGoal(goal);
			jsonGoals.put(i, goal);
		}
		return jsonGoals;
	}
	
	
	public JSONObject updateGoal(JSONObject goal) throws Exception{							
		//Initialize the variables
		System.out.println("1");
		Date createdDate = DatePatterns.dateParser(goal.getString("createdDate"));
		Date dueDate = DatePatterns.dateParser(goal.getString("dueDate"));
		int user = userId;		
		JSONObject hmTypeJson = goal.getJSONObject("hmType");
		int hmType = hmTypeJson.getInt("id");
		float reachedValue = 0;
		//getting the objective (>,<,>=,...)
		String objective = goal.getString("objective");
		
		//getting the actual goal value
		float goalValue = 0;
		try{
			goalValue = goal.getLong ("value");
		}catch(Exception e){
			System.out.println("exception float parsing[2]");
			try{
				goalValue = goal.getInt("value");
			}catch(Exception e2){
				System.out.println("exception float parsing[3]");
			}
		}		
		reachedValue = getReachedValue(hmType, user,  createdDate,  dueDate);
		//Verifying the objective, if so, update the goal to achieved
		if(verifyObjective(objective, reachedValue, goalValue)){
			goal.put("achieved", 1);				
			System.out.println("ACHIEVED GOAL");
			/*
			 * HERE COMES THE UPDATE TO THE DATABASE
			 */								
		}else{System.out.println("GOAL NOT ACHIEVED"); }
		return goal;		
	}
	
	public static float getReachedValue(int hmTypeId, int user, Date startDate, Date dueDate){		
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
		
		return 0;
	}
	
	public static float sumMeasures(int hmTypeId, int user, Date startDate, Date dueDate){
		//take the HealthMeasures from the hmType
		Response r = HealthMeasureDataClient.getHealthMeasures(user, hmTypeId, startDate, dueDate);
		//JSONObject healthMeasuresJSON = HealthMeasureDataClient.getHealthMeasures(user, hmTypeId, startDate, dueDate);
		JSONArray measures = new JSONArray(r.readEntity(String.class));
		float reachedValue = 0;
		//for each measure
		if (measures.length()==0)
			return 0;
		for(int j=0; j<measures.length(); j++){
			JSONObject measure = measures.getJSONObject(j);
			
			//take the value of the measure and sum to the totalValue
			
			try{ 
				reachedValue += measure.getLong("value");				
			}catch(Exception e){
				System.out.println("exception float parsing");
				try{
					reachedValue += measure.getInt("value");
				}catch(Exception ex){}
				
			}
		}
		return reachedValue;
	}
	
	public static float lastMeasure(int hmTypeId, int user, Date startDate, Date dueDate){
		//take the HealthMeasures from the hmType
		Response r = HealthMeasureDataClient.getHealthMeasures(user, hmTypeId, startDate, dueDate);
		
		JSONArray measures = new JSONArray(r.readEntity(String.class));
		//Take the last object and its value
		if (measures.length()==0)
			return 0;
		JSONObject measure = measures.getJSONObject(measures.length()-1);		
		Float value;
		try{ 
			value = (float) measure.getLong("value");
			return value;
		}catch(Exception e){
			System.out.println("exception float parsing");
			try{
				value = (float) measure.getInt("value");
				return value;
			}catch(Exception ex){}
			
			
		}		
		return 0;
	}
	
	//Verify if the first value attend to the objective when compared to the second
	public static boolean verifyObjective(String objective, float v1, float v2){
		switch(objective){
			case ">":
				return (v1>v2);				
			case ">=":
				return (v1>=v2);
			case "<":
				return(v1<v2);
			case "<=":
				return(v1<=v2);
			case "=":
				return (v1==v2);
		}
		return false;
	}
	
	public static JSONArray cloneGoals(JSONArray jsonGoals) throws Exception{
		
		//perform the JSON Update goal for all elements of the JSONArray
		for(int i = 0; i<jsonGoals.length(); i++){
			JSONObject goal = jsonGoals.getJSONObject(i);
			goal = cloneGoal(goal);
			jsonGoals.put(i, goal);
		}
		return jsonGoals;
	}
	
	public static JSONObject cloneGoal(JSONObject goal) throws Exception{
		String frequency = goal.getString("frequency");
		boolean hasFreuency = false;
		goal.put("achieved", 0);
		frequency = goal.getString("frequency");
		if (!frequency.equalsIgnoreCase("daily"))
			return goal;
		Date today = new Date();
		GoalDataClient.RegisterAndChangeDate(goal, today, today);
		
		return goal;
	}
	
	
}
