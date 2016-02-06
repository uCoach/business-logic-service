package ucoach.businesslogic.rest.control;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

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
	
	public static JSONObject updateGoals(JSONObject jsonGoals) throws Exception{				
		
		//Take the goals Array from the given JSON		
		JSONArray goals = jsonGoals.getJSONArray("goals");
		
		//Take the goals one by one
		for(int i = 0; i<goals.length(); i++){	
			
			//Initialize the variables
			JSONObject goal = goals.getJSONObject(i);
			Date createdDate = dateParser(goal.getString("createdDate"));
			Date dueDate = dateParser(goal.getString("dueDate"));
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
			if(!hasFrequency){
				reachedValue = getReachedValue(hmType, user,  createdDate,  dueDate);
				System.out.println("DO NOT CREATE A NEW GOAL");
			}else{
				System.out.println("CREATE A NEW GOAL");
				Date today = new Date();
				Date yesterday = getYesterdayDate();
				reachedValue = getReachedValue(hmType, user, createdDate, yesterday);				
				/*
				 * HERE COMES THE CREATING A NEW GOAL
				 */
			}
									
			
			
			//Verifying the objective, if so, update the goal to achieved
			if(verifyObjective(objective, reachedValue, goalValue)){
				goal.put("achieved", 1);
				goals.put(i, goal);
				
				System.out.println("ACHIEVED GOAL");
				/*
				 * HERE COMES THE UPDATE TO THE DATABASE
				 */
			}			
		}
		//Re-wrap the Array into a JSONObject and send it
		return new JSONObject().put("goals", goals) ;
	}
	
	public static float getReachedValue(int hmType, int user, Date startDate, Date dueDate){		
		/*
		 * HERE COMES THE GET FOR THE hmTypeName
		 */
		//get the hmType name
		String hmTypeName = "steps";	
		//weight, steps, calories, 
		switch (hmTypeName){
			case "steps":
				return sumMeasures(hmType,user, startDate, dueDate);
			case "km":
				return sumMeasures(hmType,user, startDate, dueDate);
			case "weight":
				return lastMeasure(hmType,user, startDate, dueDate);
			
		}
		
		return 0;
	}
	
	public static float sumMeasures(int hmType, int user, Date startDate, Date dueDate){
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
	
	public static float lastMeasure(int hmType, int user, Date startDate, Date dueDate){
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
	
	public static Date getYesterdayDate(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}
	
	public static String dateFormater(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}
	
	public static Date dateParser(String date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("deu ruim");
			return null;
		}
	}
}
