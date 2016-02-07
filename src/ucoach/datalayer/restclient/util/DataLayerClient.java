package ucoach.datalayer.restclient.util;

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

public class DataLayerClient {
	
	private static JSONObject getUserById(long id){
		
		return null;
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
				Authorization.authorizeRequest(builder);
				// GET request
				return builder.post(Entity.entity(json.toString(), mediaType));
	}
	

	/*
	 * Include the authorization information
	 * Header information about media type
	 * Make the request and returns the response
	 * */
	public static Response fetchGetResponse(final WebTarget target, String mediaType) {	
		Builder builder = target.request().accept(mediaType);
		Authorization.authorizeRequest(builder);
		return builder.get();
	}
	
	private static URI getBaseURI() {
        return UriBuilder.fromUri(ServiceVars.DATA_LAYER_ADDRESS).build();
    }
}
