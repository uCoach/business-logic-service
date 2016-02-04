package ucoach.businesslogic.rest.resources;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import ucoach.util.Authorization;

import javax.ws.rs.core.Response;

@Path("/register")
public class UserRegister {
	@Context
	UriInfo uriInfo;
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON })
	public Response registerUser(@Context HttpHeaders headers, String requestBody) throws Exception{
		Response rs;
		
		
		if(! Authorization.validateRequest(headers)){
			rs = Response.status(401).build();
			return rs;
		}
		
		
		
		return null;
	}
}
