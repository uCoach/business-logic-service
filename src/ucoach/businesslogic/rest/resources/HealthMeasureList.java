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
	private int measureTypeId;
	HttpHeaders headers;
	
	public HealthMeasureList(int id, int measureTypeId){
		this.id = id;
		this.measureTypeId = measureTypeId;
	}
	public HealthMeasureList(int id){
		this.id = id;
		this.measureTypeId = 0;
		
	}
	
	/**
	 * Verify the client authentication
	 * In case the MeasureTypeId is defined, includes on the response only that kind of measure
	 * In case the MeasureTypeId id not defined, includes all the measures for that person
	 * @return the List of HealthMeasures into a JSON response
	 * @throws Exception 
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON })
	public Response getMeasures(@Context HttpHeaders headers){
		Response response = null;
		if(! Authorization.validateRequest(headers)){
			response = Response.status(401).build();
			return response;
		}
		
		if(measureTypeId==0){
			org.json.JSONObject obj = new org.json.JSONObject();
			obj.put("user", id);
			obj.put("idType", 12);
			obj.put("value", "99");
			obj.put("measurement", "Kg");
			org.json.JSONObject obj2 = new org.json.JSONObject();
			obj2.put("user", id);
			obj2.put("idType", "weight");
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
			obj.put("type", measureTypeId);
			obj.put("value", "1000");
			obj.put("measurement", "BPM");		
			response = Response.accepted(obj.toString()).build();					
		}
		return response;
		
	}
}
