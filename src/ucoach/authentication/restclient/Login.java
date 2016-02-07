package ucoach.authentication.restclient;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONObject;

import ucoach.businesslogic.rest.manager.ServiceVars;
import ucoach.util.JsonParser;



public class Login {
	private String user;
	private String password;
	public Login(String user, String password){
		this.user = user;
		this.password = password;
		
		
	}
	
	public String getToken(){
		JSONObject loginJson = new JSONObject();
		JsonParser jsonParser = new JsonParser();
		loginJson.put("username", this.user);
		loginJson.put("password", this.password);
		System.out.println(loginJson);
		WebTarget baseTarget = getWebTarget();
		WebTarget target = baseTarget
				.path("login");
		
		try{
			Response r = fetchPostResponse(target, "application/json", loginJson);
			JSONObject tokenJson = new JSONObject();			
			jsonParser.loadJson(r.readEntity(String.class));
			return jsonParser.getElement("token") ; 
		}catch(Exception ex){
			return null;
		}
	}
	
	
	public static WebTarget getWebTarget(){
		ClientConfig config = new ClientConfig().register(new JacksonFeature());
		Client client = ClientBuilder.newClient(config);		
		WebTarget baseTarget = client.target(getBaseURI() );
		return baseTarget;
	}
	
	public static Response fetchPostResponse(final WebTarget target, String mediaType, JSONObject json) {
		
		// Build request
				Builder builder = target.request().accept(mediaType);
				// GET request
				return builder.post(Entity.entity(json.toString(), mediaType));
	}
	
	private static URI getBaseURI() {
        return UriBuilder.fromUri(ServiceVars.AUTHENTICATION_API_ADDRESS).build();
    }
}
