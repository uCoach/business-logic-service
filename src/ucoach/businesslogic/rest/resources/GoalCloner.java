package ucoach.businesslogic.rest.resources;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONArray;

import ucoach.businesslogic.rest.manager.GoalManager;
import ucoach.datalayer.restclient.GoalDataClient;
import ucoach.util.Authorization;
import ucoach.util.DatePatterns;

public class GoalCloner {
	
	@Context
	UriInfo uriInfo;
	int userId;
	
	/**
	 * 
	 * @param userid
	 */
	public GoalCloner(int userid){
		this.userId = userid;
	}

	/*
	 * This post receives a JSONArray with a list of GOALS and return them with the updated status
	 * in case of daily goals, it creates (if necessary) the new goal
	 * */
	@GET
	@Produces({MediaType.APPLICATION_JSON })
	 public Response cloneGoals(@Context HttpHeaders headers) throws Exception {

		if(! Authorization.validateRequest(headers)){
			return  Response.status(401).build();
		}
		
		if (this.userId == 0) {
			return Response.status(401).build();
		}

		Date yesterday = DatePatterns.getYesterdayDate();

		try{						
			Response res = GoalDataClient.getDailyGoalsFromUser(userId, yesterday, null);		
			if (res.getStatus() != 200 && res.getStatus() != 202) throw new Exception();
			
			JSONArray goals = GoalManager.cloneDailyGoals(new JSONArray(res.readEntity(String.class)));
			return Response.accepted(goals.toString()).build();

		}catch(Exception e){
			System.out.println(e.getMessage());
			return Response.status(500).build();
		}
	}
}
