package ucoach.businesslogic.rest.resources;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ucoach.util.Authorization;

public class HealthMeasure {
	private int idUser;
	private int idMeasure;
	
	/**
	 * Constructor method when the idMeasure is defined
	 */
	public HealthMeasure(int idUser, int idMeasure){
		this.idUser = idUser;
		this.idMeasure = idMeasure;
	}
	
	/**
	 * Constructor method when the idMeasure is not defined (new one)
	 */
	public HealthMeasure(int idUser){
		this.idUser = idUser;
	}
	
	/**
	 * Verify the client authentication
	 * @return the HealthMeasure into a JSON
	 * @throws Exception 
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON })
	public Response getMeasure(@Context HttpHeaders headers){
		Response response;
		if(! Authorization.validateRequest(headers)){
			response = Response.status(401).build();
			return response;
		}		
		org.json.JSONObject obj = new org.json.JSONObject();
		obj.put("idUser", idUser);
		obj.put("idMeasure", idMeasure);
		obj.put("idType", 2);
		obj.put("value", "1000");
		obj.put("measurement", "Kg");		
		response = Response.accepted(obj.toString()).build();	
		
		return response;
	}
	
	/**
	 * Verify the client authentication
	 * Delete the HealthMeasure if it exists
	 * @return the final status of the request
	 * @throws Exception 
	 */
	
	@DELETE
	@Produces({MediaType.APPLICATION_JSON })
	public Response deleteMeasure(@Context HttpHeaders headers){
		Response response;
		if(! Authorization.validateRequest(headers)){
			response = Response.status(401).build();
			return response;
		}
		
		response = Response.status(200).build();
		return response;
	}
	
	/**
	 * Verify the client authentication
	 * Try to add the new HealthMeasure
	 * @return the HealthMeasure into a JSON with its respective Id
	 * @throws Exception 
	 */
	@POST
	@Consumes({MediaType.APPLICATION_JSON })
	public Response postNewMeasure(@Context HttpHeaders headers, String body){
		Response response;
		if(! Authorization.validateRequest(headers)){
			response = Response.status(401).build();
			return response;
		}
		
		org.json.JSONObject obj = new org.json.JSONObject();
		obj.put("idUser", idUser);
		obj.put("idMeasure", 666);
		obj.put("idType", 1);
		obj.put("value", "1000");
		obj.put("measurement", "Kg");		
		response = Response.accepted(obj.toString()).build();	
		
		return response;
		
	}
	
}
