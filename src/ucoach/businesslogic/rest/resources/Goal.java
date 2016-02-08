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

import org.json.JSONObject;

import ucoach.businesslogic.rest.manager.GoalManager;
import ucoach.datalayer.restclient.GoalDataClient;
import ucoach.util.Authorization;

public class Goal {

	private int userId;
	String dateFrom;

	/**
	 * 
	 * @param userId
	 */
	public Goal(int userId) {
		this.userId = userId;
	}
	
	/**
	 * Verify the client authentication
	 * Add a new Goal
	 * @return the Goal Object with its new ID
	 * @throws Exception 
	 */
	@POST
	@Consumes({MediaType.APPLICATION_JSON })
	public Response postSingleGoal(@Context HttpHeaders headers, String body) throws Exception {
		
		Authorization.validateRequest(headers);
		if(! Authorization.validateRequest(headers)){
			return Response.status(401).build();
		}
		
		if (this.userId == 0) {
			return Response.status(401).build();
		}

		JSONObject goal = new JSONObject(body);
		goal.put("userId", userId);
		return GoalDataClient.registerGoal(goal);
	}
}
