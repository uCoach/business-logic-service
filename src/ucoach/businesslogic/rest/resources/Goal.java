package ucoach.businesslogic.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ucoach.util.Authorization;


public class Goal {
	private int userId;
	private int goalId;
	
	public Goal (int userId){
		this.userId = userId;
		this.goalId = 0;
	}
	
	public Goal (int userId, int goalId){
		this.userId = userId;
		this.goalId = goalId;
	}
	
	/**
	 * Verify the client authentication
	 * Add a new Goal
	 * @return the Goal Object with its new ID
	 * @throws Exception 
	 */
	@POST
	@Consumes({MediaType.APPLICATION_JSON })
	 public Response postSingleGoal(@Context HttpHeaders headers) throws Exception {
		Authorization.validateRequest(headers);
		Response response;
		if(! Authorization.validateRequest(headers)){
			response = Response.status(401).build();
			return  response;
		}
		org.json.JSONObject obj = new org.json.JSONObject();
		obj.put("id", 1);
		obj.put("frequency", "daily");
		obj.put("objective", "100");
		obj.put("value", "100");
		obj.put("due_date", "2016/03/01");
		obj.put("achieved", "No");
		obj.put("hmType", 1);
		obj.put("user", userId);
		response = Response.accepted(obj.toString()).build();
			
		return  response;
	}
	
	/**
	 * Verify the client authentication
	 * Update the Goal
	 * @return the up to date Goal Object
	 * @throws Exception 
	 */
	@PUT
	@Consumes({MediaType.APPLICATION_JSON })
	 public Response updateSingleGoal(@Context HttpHeaders headers) throws Exception {
		Authorization.validateRequest(headers);
		Response response;
		if(! Authorization.validateRequest(headers)){
			response = Response.status(401).build();
			return  response;
		}
		if(this.goalId==0){
			response = Response.status(404).build();
			return response;
		}
		org.json.JSONObject obj = new org.json.JSONObject();
		obj.put("id", goalId);
		obj.put("frequency", "daily");
		obj.put("objective", "100");
		obj.put("value", "100");
		obj.put("due_date", "2016/03/01");
		obj.put("achieved", "No");
		obj.put("hmType", 1);
		obj.put("user", userId);
		response = Response.accepted(obj.toString()).build();
			
		return  response;
	}
	
	/**
	 * Verify the client authentication
	 * Verify if the GoalId is set
	 * @returns the goal Object
	 * @throws Exception 
	 */	
	@GET
	@Produces({MediaType.APPLICATION_JSON })
	 public Response getPerson(@Context HttpHeaders headers) throws Exception {
		Response response;
		if(! Authorization.validateRequest(headers)){
			response = Response.status(401).build();
			return  response;
		}
		if(this.goalId==0){
			response = Response.status(404).build();
			return response;
		}
		org.json.JSONObject obj = new org.json.JSONObject();
		obj.put("id", goalId);
		obj.put("frequency", "daily");
		obj.put("objective", "100");
		obj.put("value", "100");
		obj.put("due_date", "2016/03/01");
		obj.put("achieved", "No");
		obj.put("hmType", 1);
		obj.put("user", userId);
		response = Response.accepted(obj.toString()).build();
			
		return  response;
	}
	
	/**
	 * Verify the client authentication
	 * Delete the Goal if it exists
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
		if(this.goalId==0){
			response = Response.status(404).build();
			return response;
		}
		
		response = Response.status(200).build();
		return response;
	}
	
	
}