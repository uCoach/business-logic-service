package ucoach.datalayer.restclient;

import java.util.Date;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import ucoach.datalayer.restclient.util.DataLayerClient;
import ucoach.util.DatePatterns;
import ucoach.util.JsonParser;

public class HealthMeasureDataClient {
	static JsonParser jsonParser = new JsonParser();
	/*
	 * Register of HealthMeasures
	 * Receive the variables and returns an Response object with the created Measure 
	 * */
	public static Response registerHealthMeasure(int userId, int typeId, float value, Date createdDate) throws Exception{	
		WebTarget baseTarget = DataLayerClient.getWebTarget();
		WebTarget target = baseTarget
				.path("measure");
		String created = DatePatterns.dateFormater(createdDate);
		JSONObject measureJson = new JSONObject();
		measureJson.put("userId", userId);
		measureJson.put("type", typeId);
		measureJson.put("value", value);
		measureJson.put("createdDate", created);
		try{
			Response r = DataLayerClient.fetchPostResponse(target, "application/json", measureJson);
			return r;
		}catch(Exception ex){
			return null;
		}		
	}
	
	/*
	 * Receive the information for the get (fromDate and toDate can be null)
	 * performs a query on the data layer and return it into a Response object
	 * */
	public static Response getHealthMeasures(int userId, int typeId, Date fromDate, Date toDate){
		WebTarget baseTarget = DataLayerClient.getWebTarget();
		String getFromTo = "";
		if(fromDate != null){
			getFromTo = "?fromDate="+DatePatterns.dateFormater(fromDate);
			if(toDate != null)
				getFromTo += "&toDate="+DatePatterns.dateFormater(toDate);
		}
		WebTarget target = baseTarget
				.path("measure")
				.path("type")
				.path(typeId+"")
				.path("user")
				.path(userId+getFromTo);
		try{
			Response r = DataLayerClient.fetchGetResponse(target, "application/json");
			System.out.println(r);
			return r;
		}catch(Exception ex){
			return null;
		}
	}
	
	
}
