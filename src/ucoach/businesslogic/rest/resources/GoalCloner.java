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
	public GoalCloner(int userid){
		this.userId = userid;
	}
	/*
	 * This post receives a JSONArray with a list of GOALS and return them with the updated status
	 * in case of daily goals, it creates (if necessary) the new goal
	 * */
	@GET
	@Produces({MediaType.APPLICATION_JSON })
	 public Response updateGoals(@Context HttpHeaders headers) throws Exception {
		if(! Authorization.validateRequest(headers)){
			return  Response.status(401).build();
		}
		
		JSONArray goals; new JSONArray();		
		Date yesterday = DatePatterns.getYesterdayDate();
		GoalManager goalmng = new GoalManager(userId);
		try{						
			Response r = GoalDataClient.getDailyGoalsFromUser(userId, yesterday, null);			
			goals = new JSONArray(r.readEntity(String.class));
			//System.out.println(goals);
			goals = goalmng.cloneGoals(goals);
			//System.out.println(goals);
		}catch(Exception e){
			System.out.println(e);
			return Response.status(400).build();
		}
		
		
		return Response.accepted(goals.toString()).build();
	}
}
