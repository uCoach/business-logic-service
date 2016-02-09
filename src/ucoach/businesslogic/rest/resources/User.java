package ucoach.businesslogic.rest.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.json.JSONObject;

import ucoach.authentication.restclient.Authenticator;
import ucoach.authentication.restclient.Login;
import ucoach.businesslogic.rest.manager.JSONBuilder;
import ucoach.datalayer.restclient.UserDataClient;
import ucoach.util.*;

@Path("/user")
public class User {

	@Context
	UriInfo uriInfo;	
	
	static JsonParser jsonParser = new JsonParser();
	
	/**
	 * Verify the client authentication
	 * Add a new User
	 * @return the User Object with its new ID
	 * @throws Exception 
	 */
	@POST
	@Consumes({MediaType.APPLICATION_JSON })
	public Response registerUser(@Context HttpHeaders headers, String requestBody) throws Exception{

		if(! Authorization.validateRequest(headers)) {
			return Response.status(401).build();
		}

		Response response = UserDataClient.registerUser(new JSONObject(requestBody));
		if (response == null) return Response.status(500).build();		

		return Response.ok(response.toString()).build();
	}
	
	/**
	 * Get User info
	 * @return the User from the given ID
	 * @throws Exception 
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON })
    public Response getUser(@Context HttpHeaders headers) throws Exception {

		if(!Authorization.validateRequest(headers)){
			return Response.status(401).build();
		}

		// Get userId by token
		String userToken = headers.getHeaderString("User-Authorization");
		long userId = Authenticator.authenticate(userToken);
		if(userId == 0) {
			return Response.status(401).build();
		}

		return UserDataClient.getUserById(userId);
	}
	
	@GET
	@Path("/google/authorization")
	@Produces({MediaType.APPLICATION_JSON })
  public Response authorizeGoogleApi(@Context HttpHeaders headers) throws Exception {
		if(!Authorization.validateRequest(headers)){
			return Response.status(401).build();
		}

		// Get userId by token
		String userToken = headers.getHeaderString("User-Authorization");
		long userId = Authenticator.authenticate(userToken);
		if(userId == 0) return Response.status(401).build();
		
		Response r = UserDataClient.authorizeUserGoogle(userId);
		if (r == null) return Response.status(401).build();
		
		// Parser response
		String jsonResponse = r.readEntity(String.class);
		
		return Response.ok(jsonResponse).build();
	}

	/**
	 * Just object Request
	 * Implements the methods for the current user regarding all the measures with the type {measure}
	 * Updates the User for the requester {ID}
	 * @throws Exception 
	 */
	@Path("measurelist/{typeId}")
	public HealthMeasureList getHealthMeasureList(@Context HttpHeaders headers, @PathParam("typeId") int measureTypeId) throws Exception{
		
		// Get userId by token
		String userToken = headers.getHeaderString("User-Authorization");
		int userId = (int) Authenticator.authenticate(userToken);

		return new HealthMeasureList(userId, measureTypeId);		
	}
	
	/**
	 * Just object Request
	 * Implements the methods for the current user regarding single new measure (POST)
	 * @throws Exception 
	 */
	@Path("measure")
	public HealthMeasure getHealthMeasure(@Context HttpHeaders headers) throws Exception{
		
		// Get userId by token
		String userToken = headers.getHeaderString("User-Authorization");
		int userId = (int) Authenticator.authenticate(userToken);

		return new HealthMeasure(userId);
	}
	
	/**
	 * Just object Request
	 * Implements the methods for the current user regarding single new GOAL (POST)
	 * Implements the metos for the current user regarding undefined GOALS
	 * @throws Exception 
	 */
	@Path("goal")
	public Goal newGoal(@Context HttpHeaders headers) throws Exception {
		
		// Get userId by token
		String userToken = headers.getHeaderString("User-Authorization");
		int userId = (int) Authenticator.authenticate(userToken);

		return new Goal(userId);
	}
	
	@Path("goal-update")
	public GoalUpdater updateGoal(@QueryParam("dateFrom") String dateFrom, @Context HttpHeaders headers) throws Exception {
		
		// Get userId by token
		String userToken = headers.getHeaderString("User-Authorization");
		int userId = (int) Authenticator.authenticate(userToken);

		return new GoalUpdater(userId, dateFrom);
	}

	/**
	 * Just object Request
	 * Implements the methods for the current user regarding single new GOAL (POST)
	 * Implements the methods for the current user regarding undefined GOALS
	 * @throws Exception 
	 */
	@Path("goalslist")
	public GoalList getGoalsByType(@QueryParam("achieved") String achieved, @Context HttpHeaders headers) throws Exception {

		// Get userId by token
		String userToken = headers.getHeaderString("User-Authorization");
		int userId = (int) Authenticator.authenticate(userToken);
		
		return new GoalList(userId, achieved);
	}
}
