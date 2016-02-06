package ucoach.businesslogic.rest.control;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import ucoach.util.JsonParser;

public class GoalController {
	static JsonParser jsonParser = new JsonParser();
	
	public static JSONObject updateGoals(JSONObject jsonGoals) throws Exception{				
		//Take the goals Array from the given JSON		
		JSONArray goals = jsonGoals.getJSONArray("goals");
		
		//Take the goals one by one
		for(int i = 0; i<goals.length(); i++){			
			JSONObject goal = goals.getJSONObject(i);
			
			//Take the HM code of the goal
			int hmId = goal.getInt("hmType");
			
			//take the HealthMeasures from the hmType
			JSONObject healthMeasuresJSON = Pretender.getHealthMeasureSteps();
			JSONArray measures = healthMeasuresJSON.getJSONArray("measures");
			
			
			
			
			
		}
		return null;
	}
}
