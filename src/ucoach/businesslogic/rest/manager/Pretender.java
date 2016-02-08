package ucoach.businesslogic.rest.manager;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.json.JSONObject;

public class Pretender {
	//objective >+ <, 
	public static JSONObject getGoals(){
		org.json.JSONObject obj = new org.json.JSONObject();
		obj.put("id", 1);
		obj.put("frequency", "daily");
		obj.put("objective", ">=");
		obj.put("value", "5000");
		obj.put("createdDate", "2016-02-05");
		obj.put("dueDate", "2016-03-01");		
		obj.put("achieved", 0);
		obj.put("hmType", 1);
		obj.put("user", 1);
		
		
		org.json.JSONObject obj1 = new org.json.JSONObject();
		obj1.put("id", 1);
		//obj1.put("frequency", "daily");
		obj1.put("objective", ">=");
		obj1.put("value", "1000");
		obj1.put("createdDate", "2016-03-05");
		obj1.put("dueDate", "2016-03-01");
		
		obj1.put("achieved", 0);
		obj1.put("hmType", 2);
		obj1.put("user", 2);
		
		
		org.json.JSONObject obj2 = new org.json.JSONObject();
		obj2.put("id", 1);
		obj2.put("frequency", "");
		obj2.put("objective", ">");
		obj2.put("value", "2000");
		obj2.put("createdDate", "2016-03-05");
		obj2.put("dueDate", "2016-03-01");		
		obj2.put("achieved", 0);
		obj2.put("hmType", 2);
		obj2.put("user", 2);
		
		
		List<JSONObject> objs = new ArrayList<JSONObject>();
		objs.add(obj);
		objs.add(obj1);
		objs.add(obj2);
		
		
		return new JSONObject().put("goals", objs).put("size", 3) ;
	}
	
	public static JSONObject getHealthMeasureSteps(){
		org.json.JSONObject obj = new org.json.JSONObject();
		obj.put("id", 1);
		obj.put("user", 1);
		obj.put("date", "2016-02-05");
		obj.put("hmType", 1);
		obj.put("value", "1001");
		
		
		org.json.JSONObject obj2 = new org.json.JSONObject();
		obj2.put("id", 2);
		obj2.put("user", 1);
		obj2.put("date", "2016-02-05");
		obj2.put("hmType", 1);
		obj2.put("value", "3999");
			
		List<JSONObject> ljs = new ArrayList();
		ljs.add(obj);
		ljs.add(obj2);
		JSONObject objs = new org.json.JSONObject();		
		objs.put("measures", ljs);
		
		return objs;
	}
	
	public static JSONObject getSingleGoal(){
		org.json.JSONObject obj = new org.json.JSONObject();
		obj.put("id", 1);
		obj.put("frequency", "daily");
		obj.put("objective", ">=");
		obj.put("value", "5000");
		obj.put("createdDate", "2016-02-05");
		obj.put("dueDate", "2016-03-01");		
		obj.put("achieved", 0);
		obj.put("hmType", 1);
		obj.put("user", 1);
		return obj;
	}
	
	public static JSONObject getSingleHealthMeasure(){
		org.json.JSONObject obj = new org.json.JSONObject();
		obj.put("id", 1);
		obj.put("user", 1);
		obj.put("date", "2016-02-05");
		obj.put("hmType", 1);
		obj.put("value", "1001");
		return obj;
	}
	
	public static JSONObject getUser(){
		org.json.JSONObject obj = new org.json.JSONObject();
		obj.put("id", 1);	
		obj.put("email", "dowjones@umbrella.com");
		obj.put("name", "Fred");
		obj.put("lastname", "Flowers");
		obj.put("birthdate", "1990-05-02");
		//obj.put("twitterusername", "pefabiodemelo");
		obj.put("password", "aehuaheu21432uhquahuhqu423uauh1u23");
		
		return obj;
			
	}
}
