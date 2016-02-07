package ucoach.businesslogic.rest.manager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import ucoach.util.DatePattern;
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
		Date createdDate = DatePattern.dateParser(goal.getString("createdDate"));
		Date dueDate = DatePattern.dateParser(goal.getString("dueDate"));
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
		String hmTypeName = "steps";
		if(!hasFrequency){
			reachedValue = getReachedValue(hmTypeName, user,  createdDate,  dueDate);
			System.out.println("DO NOT CREATE A NEW GOAL");
		}else{
			System.out.println("CREATE A NEW GOAL");
			Date today = new Date();
			Date yesterday = DatePattern.getYesterdayDate();
			reachedValue = getReachedValue(hmTypeName, user, createdDate, yesterday);				
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
		}
		return goal;		
	}
	
	public static float getReachedValue(String hmTypeName, int user, Date startDate, Date dueDate){		
		/*
		 * HERE COMES THE GET FOR THE hmTypeName
		 */
		//get the hmType name
			
		//weight, steps, calories, 
		switch (hmTypeName){
			case "steps":
				return sumMeasures(hmTypeName,user, startDate, dueDate);
			case "km":
				return sumMeasures(hmTypeName,user, startDate, dueDate);
			case "weight":
				return lastMeasure(hmTypeName,user, startDate, dueDate);
			
		}
		
		return 0;
	}
	
	public static float sumMeasures(String hmTypeName, int user, Date startDate, Date dueDate){
		//take the HealthMeasures from the hmType
		JSONObject healthMeasuresJSON = Pretender.getHealthMeasureSteps();
		JSONArray measures = healthMeasuresJSON.getJSONArray("measures");
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
	
	public static float lastMeasure(String hmTypeName, int user, Date startDate, Date dueDate){
		//take the HealthMeasures from the hmType
		JSONObject healthMeasuresJSON = Pretender.getHealthMeasureSteps();
		JSONArray measures = healthMeasuresJSON.getJSONArray("measures");
		
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
