package ucoach.businesslogic.rest.resources;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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


public class GoalUpdater {
	
	@Context
	UriInfo uriInfo;
	int userId;
	String dateFrom;
	
	/**
	 * 
	 * @param userid
	 * @param dateFrom
	 */
	public GoalUpdater(int userid, String dateFrom){
		this.userId = userid;
		this.dateFrom = dateFrom;
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON })
	 public Response updateGoals(@Context HttpHeaders headers) throws Exception {
		if(! Authorization.validateRequest(headers)){
			return  Response.status(401).build();
		}
		
		if (this.userId == 0) {
			return Response.status(401).build();
		}

		JSONArray goals;		
		Date date = DatePatterns.dateParser(dateFrom);
		GoalManager goalmng = new GoalManager(userId);
		try{			
			Response r = GoalDataClient.getGoalsFromUser(userId, date, "false");			
			try{
				goals = new JSONArray(r.readEntity(String.class));
			}catch(Exception e){
				return null;
			}
			System.out.println(goals);
			goals = goalmng.updateGoals(goals);
			System.out.println(goals);
		}catch(Exception e){
			System.out.println(e);
			return Response.status(400).build();
		}
		
		return Response.accepted(goals.toString()).build();
	}
	
	@Path("clone")
	public GoalCloner cloneYesterday(@Context HttpHeaders headers) throws Exception {
		return new GoalCloner(userId);
	}
}
