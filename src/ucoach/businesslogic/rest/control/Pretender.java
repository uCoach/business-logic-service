package ucoach.businesslogic.rest.control;

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
		obj.put("value", "100");
		obj.put("dueDate", "2016/03/01");
		obj.put("createdDate", "2016/02/05");
		obj.put("achieved", 0);
		obj.put("hmType", 1);
		obj.put("user", 1);
		
		
		org.json.JSONObject obj1 = new org.json.JSONObject();
		obj1.put("id", 1);
		obj1.put("frequency", "=");
		obj1.put("objective", "100");
		obj1.put("value", "100");
		obj1.put("dueDate", "2016/03/01");
		obj.put("createdDate", "2016/02/05");
		obj1.put("achieved", 0);
		obj1.put("hmType", 1);
		obj1.put("user", 1);
		
		
		org.json.JSONObject obj2 = new org.json.JSONObject();
		obj2.put("id", 1);
		obj2.put("frequency", "<=");
		obj2.put("objective", "100");
		obj2.put("value", "100");
		obj2.put("dueDate", "2016/03/01");
		obj.put("createdDate", "2016/02/05");
		obj2.put("achieved", 0);
		obj2.put("hmType", 1);
		obj2.put("user", 1);
		
		
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
		obj.put("date", "2015-12-12");
		obj.put("hmType", 1);
		obj.put("value", "1000");
		
		
		org.json.JSONObject obj2 = new org.json.JSONObject();
		obj2.put("id", 2);
		obj2.put("user", 1);
		obj2.put("date", "2015-12-13");
		obj2.put("hmType", 1);
		obj2.put("value", "1000");
			
		List<JSONObject> ljs = new ArrayList();
		ljs.add(obj);
		ljs.add(obj2);
		JSONObject objs = new org.json.JSONObject();		
		objs.put("measures", ljs);
		
		return objs;
	}
}
