package ucoach.businesslogic.rest.resources;

import javax.ws.rs.Consumes;
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
		Response rs;
		if(! Authorization.validateRequest(headers)){
			rs = Response.status(401).build();
			return rs;
		}
		//Try to add the user
		Response registerResponse = UserDataClient.registerUser( new JSONObject(requestBody));
		
		if(registerResponse.getStatus()==200 || registerResponse.getStatus()==202 ){
			
			String userJson = registerResponse.readEntity(String.class);
			jsonParser.loadJson(userJson);
			
			String username = jsonParser.getElement("email");
			String password = jsonParser.getElement("password");
			
			Login login = new Login(username, password);
			String token = login.getToken();
			System.out.println(token);
			JSONObject finalresponse = JSONBuilder.singleValueJsonResponse(new JSONObject(userJson), token, "token");
			rs = Response.accepted(finalresponse.toString()).build();
			return rs;
			
		}else{
			return registerResponse;
		}
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
	
	/**
	 * Just object Request
	 * Implements the methods for the current user regarding all the measures with the type {measure}
	 * Updates the User for the requester {ID}
	 * @throws Exception 
	 */
	@Path("measurelist/{measure}")
	public HealthMeasureList getHealthMeasureList(@Context HttpHeaders headers, @PathParam("measure") int measureTypeId) throws Exception{
		
		// Get userId by token
		String userToken = headers.getHeaderString("User-Authorization");
		int userId = (int) Authenticator.authenticate(userToken);

		return new HealthMeasureList(userId, measureTypeId);		
	}
	
	/**
	 * Just object Request
	 * Implements the methods for the current user regarding all the measures
	 * Updates the User for the requester {ID}
	 * @throws Exception 
	 */
	@Path("measurelist")
	public HealthMeasureList getAllHealthMeasureLists(@Context HttpHeaders headers) throws Exception {
		// Get userId by token
		String userToken = headers.getHeaderString("User-Authorization");
		int userId = (int) Authenticator.authenticate(userToken);
			
		return new HealthMeasureList(userId);		
	}

	/**
	 * Just object Request
	 * Implements the methods for the current user regarding the measure {IDMeasure}
	 */
	@Path("{ID}/measure/{IDMeasure}")
	public HealthMeasure getHealthMeasure (@PathParam("ID") int idUser, @PathParam("ID") int idMeasure){
		HealthMeasure hm = new HealthMeasure(idUser, idMeasure);
		return hm;
	}
	
	/**
	 * Just object Request
	 * Implements the methods for the current user regarding single new measure (POST)
	 */
	@Path("{ID}/measure")
	public HealthMeasure getHealthMeasure (@PathParam("ID") int idUser){
		HealthMeasure hm = new HealthMeasure(idUser);
		return hm;
	}
	
	/**
	 * Just object Request
	 * Implements the methods for the current user regarding single new GOAL (POST)
	 * Implements the metos for the current user regarding undefined GOALS
	 */
	@Path("{ID}/goal")
	public Goal getGoal(@PathParam("ID") int idUser){
		Goal g = new Goal(idUser);
		return g;
	}
	
	
	
	/**
	 * Just object Request
	 * Implements the methods for the current user regarding single new GOAL (POST)
	 * Implements the methods for the current user regarding undefined GOALS
	 */
	
	@Path("{idUser}/goalsList/")
	public GoalList getGoalsByType(@PathParam("idUser") int idUser){
		GoalList g = new GoalList(idUser);
		return g;
	}
	
	
	
}
