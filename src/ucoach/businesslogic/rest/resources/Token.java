package ucoach.businesslogic.rest.resources;

import ucoach.authentication.restclient.*;
import ucoach.authorization.Authorization;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

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

import javax.xml.ws.WebServiceContext;

import javax.annotation.Resource;

@Path("/token/{token}")
public class Token {

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public long getPerson(@Context HttpHeaders headers, @PathParam("token") String token) throws Exception {
		
		Authorization.validateRequest(headers);
		
		if(Authorization.validateRequest(headers)){
			Long l =  Authenticator.authenticate(token);
			return l;
		}else{
			Response r = Response.noContent().build();
			return 0;
		}
		
        
    }
}
