package ucoach.businesslogic.rest.resources;

import ucoach.authentication.restclient.*;
import ucoach.util.Authorization;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.ws.WebServiceContext;

import org.json.JSONObject;

import javax.annotation.Resource;

@Path("/token/{token}")
public class Token {
	@Context
	UriInfo uriInfo;
	
	
	/**
	 * Method to return the user according to its token
	 * Verify client authorization
	 * Return the user code if the token is valid
	 * 
	 */	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response getPerson(@Context HttpHeaders headers, @PathParam("token") String token) throws Exception {		
		
		Authorization.validateRequest(headers);
		Response res;
		if(! Authorization.validateRequest(headers)){
			Response r = Response.status(401).build();
			return r;
		}
		
		try{
			Long l =  Authenticator.authenticate(token);
			JSONObject obj = new org.json.JSONObject();
			obj.put("userid", 2);
			if(l!=0){
				res = Response.status(Status.OK).entity(obj.toString()).location(uriInfo.getAbsolutePath()).build();
			}else{
				res = Response.noContent().build();
			}
							
		}catch(Exception e){
			res = Response.serverError().build();
		}
			
		return res;		      
    }
}
