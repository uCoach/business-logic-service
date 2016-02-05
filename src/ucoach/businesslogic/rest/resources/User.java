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

import ucoach.util.Authorization;
import ucoach.util.JsonParser;

@Path("/user")
public class User {
	@Context
	UriInfo uriInfo;	
	
	static JsonParser jsonParser = new JsonParser();
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON })
	public Response registerUser(@Context HttpHeaders headers, String requestBody) throws Exception{
		Response rs;
		if(! Authorization.validateRequest(headers)){
			rs = Response.status(401).build();
			return rs;
		}
		
		
		org.json.JSONObject obj = new org.json.JSONObject();
		obj.put("email", "dowjones@umbrella.com");
		obj.put("name", "Robert");
		obj.put("birthdate", "05/05/90");
		obj.put("twitterusername", "pefabiodemelo");
		obj.put("id", 15);				
		rs= Response.accepted(obj.toString()).build();
		return rs;
	}
	
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
	
	@Path("{ID}")
	@GET
	@Produces({MediaType.APPLICATION_JSON })
    public Response getUser(@Context HttpHeaders headers, @PathParam("ID") long id) throws Exception {
		Response rs;
		if(! Authorization.validateRequest(headers)){
			rs = Response.status(401).build();
			return rs;
		}		
		org.json.JSONObject obj = new org.json.JSONObject();
		obj.put("email", "dowjones@umbrella.com");
		obj.put("name", "Robert");
		obj.put("birthdate", "05/05/90");
		obj.put("twitterusername", "pefabiodemelo");
		obj.put("id", id);				
		rs= Response.accepted(obj.toString()).build();
		return rs;
	}
	
	@Path("{ID}/measurelist/{measure}")
	public HealthMeasureList getSpecificMeasureFromPerson(@Context HttpHeaders headers,@PathParam("ID") int id, @PathParam("measure") String measure){
		
		HealthMeasureList hml = new HealthMeasureList(id, measure);
		return hml;		
	}
	
	@Path("{ID}/measurelist/")
	public HealthMeasureList getMeasureFromPerson(@Context HttpHeaders headers, @PathParam("ID") int id){		
		HealthMeasureList hml = new HealthMeasureList( id);
		return hml;		
	}
}
