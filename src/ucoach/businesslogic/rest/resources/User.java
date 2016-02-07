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

import ucoach.authentication.restclient.Login;
import ucoach.businesslogic.rest.manager.JSONBuilder;
import ucoach.businesslogic.rest.manager.Pretender;
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
		
		if(registerResponse.getStatus()==200){
			String userJson = registerResponse.readEntity(String.class);
			jsonParser.loadJson(userJson);
			
			String username = jsonParser.getElement("email");
			String password = jsonParser.getElement("password");
			
			Login login = new Login(username, password);
			String token = login.getToken();
			JSONObject finalresponse = JSONBuilder.singleValueJsonResponse(new JSONObject(userJson), token, "token");
			rs = Response.accepted(finalresponse.toString()).build();
			return rs;
			
		}else{
			return registerResponse;
		}
			
				
		
		
	}
	
	/**
	 * Verify the client authentication
	 * Updates the User for the requester {ID}
	 * @return the updated User Object
	 * @throws Exception 
	 */
	@Path("{ID}")
	@PUT
	@Consumes({MediaType.APPLICATION_JSON })
	public Response updateUser(@Context HttpHeaders headers, String requestBody, @PathParam("ID") long id) throws Exception{
		Response rs;
		System.out.println(id);
		if(! Authorization.validateRequest(headers)){
			rs = Response.status(401).build();
			return rs;
		}
		
		rs= Response.accepted().build();
		return rs;
	}
	
	/**
	 * Verify the client authentication
	 * @return the User from the given ID
	 * @throws Exception 
	 */
	@Path("{ID}")
	@GET
	@Produces({MediaType.APPLICATION_JSON })
    public Response getUser(@Context HttpHeaders headers, @PathParam("ID") long id) throws Exception {
		Response rs;
		if(! Authorization.validateRequest(headers)){
			rs = Response.status(401).build();
			return rs;
		}		
		org.json.JSONObject obj = Pretender.getUser();	
		rs= Response.accepted(obj.toString()).build();
		return rs;
	}
	
	/**
	 * Just object Request
	 * Implements the methods for the current user regarding all the measures with the type {measure}
	 * Updates the User for the requester {ID}
	 */
	@Path("{ID}/measurelist/{measure}")
	public HealthMeasureList getHealthMeasureList(@PathParam("ID") int id, @PathParam("measure") int measureTypeId){
		
		HealthMeasureList hml = new HealthMeasureList(id, measureTypeId);
		return hml;		
	}
	
	/**
	 * Just object Request
	 * Implements the methods for the current user regarding all the measures
	 * Updates the User for the requester {ID}
	 */
	@Path("{ID}/measurelist/")
	public HealthMeasureList getAllHealthMeasureLists( @PathParam("ID") int id){		
		HealthMeasureList hml = new HealthMeasureList( id);
		return hml;		
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
	 * Implements the methods regarding a defined Goal
	 */
	@Path("{ID}/goal/{goalId}")
	public Goal getGoal(@PathParam("ID") int idUser, @PathParam("goalId") int idGoal){
		Goal g = new Goal(idUser, idGoal);
		//System.out.println("abobora"+idGoal);
		return g;
	}
	
	/**
	 * Just object Request
	 * Implements the methods for the current user regarding single new GOAL (POST)
	 * Implements the methods for the current user regarding undefined GOALS
	 */
	
	@Path("{idUser}/goallist/{idType}")
	public Goal getGoalsByType(@PathParam("idUser") int idUser, @PathParam("idUser") int idType){
		Goal g = new Goal(idUser, idType);
		return g;
	}
	
	
	
}
