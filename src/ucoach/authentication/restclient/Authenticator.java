package ucoach.authentication.restclient;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import ucoach.businesslogic.rest.manager.ServiceVars;
import ucoach.util.JsonParser;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class Authenticator {

	static JsonParser jsonParser = new JsonParser();
	
	/**
	 * Starts a new connection with the authentication server
	 * Send a get request with the token on the URL (server/token/verify/[token])
	 * Fetch the answer and take the used ID
	 * @return the used ID 
	 * If it has problems parsing (in case the user token doesn't exist for instance), return 0
	 * @throws Exception 
	 */
	public static long authenticate(String token) throws Exception{

		if(token == "" || token == null) return 0;

		ClientConfig config = new ClientConfig().register(new JacksonFeature());
		Client client = ClientBuilder.newClient(config);
		WebTarget baseTarget = client.target(getBaseURI() );
		WebTarget target = baseTarget
				.path("token")
				.path("verify")
				.path(token);
		try{
			
			System.out.println(target);
			Response r = target.request().accept("application/json").get();
			jsonParser.loadJson(r.readEntity(String.class));
			Long id =  Long.parseLong(jsonParser.getElement("id"));
			return id;
			
		}catch(Exception ex){
			return 0;
		}		
	}
	
	/**
	 * Return base URI
	 * @return
	 */
	private static URI getBaseURI() {
      return UriBuilder.fromUri(ServiceVars.AUTHENTICATION_API_ADDRESS).build();
  }
}


