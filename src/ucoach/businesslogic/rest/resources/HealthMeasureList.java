package ucoach.businesslogic.rest.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

import ucoach.util.Authorization;

public class HealthMeasureList {
	private int id;
	private String measure;
	HttpHeaders headers;
	
	public HealthMeasureList(int id, String measure){
		this.id = id;
		this.measure = measure;
	}
	public HealthMeasureList(int id){
		this.id = id;
		this.measure = null;
		
	}
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON })
	public Response getMeasures(@Context HttpHeaders headers){
		Response response = null;
		if(! Authorization.validateRequest(headers)){
			response = Response.status(401).build();
			return response;
		}
		
		if(measure==null){
			org.json.JSONObject obj = new org.json.JSONObject();
			obj.put("user", id);
			obj.put("type", "height");
			obj.put("value", "99");
			obj.put("measurement", "Kg");
			org.json.JSONObject obj2 = new org.json.JSONObject();
			obj2.put("user", id);
			obj2.put("type", "weight");
			obj2.put("value", "1000");
			obj2.put("measurement", "BPM");			
			List<JSONObject> ljs = new ArrayList();
			ljs.add(obj);
			ljs.add(obj2);
			JSONObject objs = new org.json.JSONObject();
			objs.put("Measures", ljs);			
			response = Response.accepted(objs.toString()).build();	
			
		}else{
			org.json.JSONObject obj = new org.json.JSONObject();
			obj.put("user", id);
			obj.put("type", measure);
			obj.put("value", "1000");
			obj.put("measurement", "BPM");		
			response = Response.accepted(obj.toString()).build();		
			
		}
		return response;
		
	}
}
