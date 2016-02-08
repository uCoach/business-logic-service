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

import ucoach.datalayer.restclient.HealthMeasureDataClient;
import ucoach.util.Authorization;

public class HealthMeasureList {
	private int userid;
	private int measureTypeId;
	HttpHeaders headers;
	
	public HealthMeasureList(int userid, int measureTypeId){
		this.userid = userid;
		this.measureTypeId = measureTypeId;
	}
	public HealthMeasureList(int userid){
		this.userid = userid;
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
			
		response = HealthMeasureDataClient.getHealthMeasures(userid, measureTypeId, null, null);				
		
		return response;
		
	}
}
