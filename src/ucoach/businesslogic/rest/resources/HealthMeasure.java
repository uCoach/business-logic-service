package ucoach.businesslogic.rest.resources;

import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import ucoach.datalayer.restclient.HealthMeasureDataClient;
import ucoach.util.Authorization;
import ucoach.util.DatePatterns;
import ucoach.util.JsonParser;

public class HealthMeasure {
	private int idUser;
	
	/**
	 * Constructor method when the idMeasure is not defined (new one)
	 */
	public HealthMeasure(int idUser){
		this.idUser = idUser;
	}
	
	/**
	 * Verify the client authentication
	 * Try to add the new HealthMeasure
	 * @return the HealthMeasure into a JSON with its respective Id
	 * @throws NumberFormatException 
	 * @throws Exception 
	 */
	@POST
	@Consumes({MediaType.APPLICATION_JSON })
	public Response postNewMeasure(@Context HttpHeaders headers, String body) throws NumberFormatException, Exception{		
		Response response;
		if(! Authorization.validateRequest(headers)){
			response = Response.status(401).build();
			return response;
		}
		
		if (this.idUser == 0) {
			return Response.status(401).build();
		}

		JsonParser jp = new JsonParser();
		jp.loadJson(body);		
		int userId = idUser;
		int typeId = Integer.parseInt(jp.getElement("typeId"));
		float value = Float.parseFloat(jp.getElement("value"));
		
		return HealthMeasureDataClient.registerHealthMeasure(userId, typeId, value, new Date());			
	}
}
