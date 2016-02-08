package ucoach.businesslogic.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/goalUploader")
public class GoalUpdater {
	@Context
	UriInfo uriInfo;
	
	
	/*
	 * 
	 * */
	@POST
	@Consumes({MediaType.APPLICATION_JSON })
	 public Response postSingleGoal(@Context HttpHeaders headers, String body) throws Exception {
		
		return null;
	}
	
}
