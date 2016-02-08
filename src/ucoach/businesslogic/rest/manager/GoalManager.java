package ucoach.businesslogic.rest.manager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

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
	
	public static JSONArray updateGoals(JSONArray jsonGoals) throws Exception{
		
		//perform the JSON Update goal for all elements of the JSONArray
		for(int i = 0; i<jsonGoals.length(); i++){
			JSONObject goal = jsonGoals.getJSONObject(i);
			goal = updateGoal(goal);
			jsonGoals.put(i, goal);
		}
		return jsonGoals;
	}
	
	
	public static JSONObject updateGoal(JSONObject goal) throws Exception{							
		//Initialize the variables
		Date createdDate = DatePatterns.dateParser(goal.getString("createdDate"));
		Date dueDate = DatePatterns.dateParser(goal.getString("dueDate"));
		int user = goal.getInt("user");
		int hmType = goal.getInt("hmType");
		String frequency = "";
		boolean hasFrequency = false;
		float reachedValue = 0;
		
		//getting the objective (>,<,>=,...)
		String objective = goal.getString("objective");
		
		//Take the HM code of the goal
		int hmId = goal.getInt("hmType");
		
		//getting the actual goal value
		float goalValue = 0;
		try{
			goalValue = Float.parseFloat(goal.getString("value"));
		}catch(Exception e){
			System.out.println("exception float parsing[2]");
		}
		
		//check if the goal is recurrent or not
		if(goal.has("frequency")){
			frequency = goal.getString("frequency");
			if (frequency!="")
				hasFrequency = true;
		}
		
		/* Checking the goals
		 * In case it is not recurrent. we can simply check the achieved goals
		 * In case it is recurrent, we will:
		 *  	-Check if the goals were achieved for yesterday
		 *  	-Create a new goal with the createdDate for today
		 */
		int hmTypeId = 2;
		if(!hasFrequency){
			reachedValue = getReachedValue(hmTypeId, user,  createdDate,  dueDate);
			System.out.println("DO NOT CREATE A NEW GOAL");
		}else{
			System.out.println("CREATE A NEW GOAL");
			Date today = new Date();
			Date yesterday = DatePatterns.getYesterdayDate();
			reachedValue = getReachedValue(hmTypeId, user, yesterday, yesterday);				
			/*
			 * HERE COMES THE CREATING A NEW GOAL
			 */
		}			
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
		 * 2 = weight
		 * 
		 */
		//get the hmType name
			
		//weight, steps, calories, 
		switch (hmTypeId){
			case 4:
				return sumMeasures(hmTypeId,user, startDate, dueDate);
			case 3:
				return sumMeasures(hmTypeId,user, startDate, dueDate);
			case 2:
				return lastMeasure(hmTypeId,user, startDate, dueDate);
			
		}
		
		return 0;
	}
	
	public static float sumMeasures(int hmTypeId, int user, Date startDate, Date dueDate){
		//take the HealthMeasures from the hmType
		Response r = HealthMeasureDataClient.getHealthMeasures(user, hmTypeId, startDate, dueDate);
		//JSONObject healthMeasuresJSON = HealthMeasureDataClient.getHealthMeasures(user, hmTypeId, startDate, dueDate);
		JSONArray measures = new JSONArray(r.toString());
		float reachedValue = 0;
		//for each measure
		for(int j=0; j<measures.length(); j++){
			JSONObject measure = measures.getJSONObject(j);
			
			//take the value of the measure and sum to the totalValue
			String value = measure.getString("value");
			try{ 
				reachedValue += Float.parseFloat(value);
			}catch(Exception e){
				System.out.println("exception float parsing");
			}
		}
		return reachedValue;
	}
	
	public static float lastMeasure(int hmTypeId, int user, Date startDate, Date dueDate){
		//take the HealthMeasures from the hmType
		
		Response r = HealthMeasureDataClient.getHealthMeasures(user, hmTypeId, startDate, dueDate);
		
		System.out.println(r.toString());
		JSONArray measures = new JSONArray(r.toString());
		
		//Take the last object and its value
		JSONObject measure = measures.getJSONObject(measures.length()-1);		
		String value = measure.getString("value");
		try{ 
			return Float.parseFloat(value);
		}catch(Exception e){
			System.out.println("exception float parsing");
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
	
	
}
