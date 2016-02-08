package ucoach.businesslogic.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ucoach.datalayer.restclient.GoalDataClient;
import ucoach.util.Authorization;

public class GoalList {
	int userId;
	String achieved;
	
	/**
	 * 
	 * @param userId
	 * @param achieved
	 */
	public GoalList(int userId, String achieved) {
		this.userId = userId;
		this.achieved = achieved;
	}
	
	@GET
	@Produces({MediaType.APPLICATION_JSON })
	public Response getGoalsList(@Context HttpHeaders headers) {
		if(! Authorization.validateRequest(headers)){
			return  Response.status(401).build();
		}

		if (this.userId == 0) {
			return Response.status(401).build();
		}

		return GoalDataClient.getGoalsFromUser(userId, null, achieved);
	}
}
