package ucoach.datalayer.restclient;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONObject;

import ucoach.datalayer.restclient.util.DataLayerClient;
import ucoach.util.JsonParser;

public class UserDataClient {
static JsonParser jsonParser = new JsonParser();

/**
 * 
 * @param userJson
 * @return
 * @throws Exception
 */
	public static Response registerUser(JSONObject userJson) throws Exception{
		WebTarget baseTarget = DataLayerClient.getWebTarget();
		WebTarget target = baseTarget
				.path("user");
		try{
			return DataLayerClient.fetchPostResponse(target, "application/json", userJson);
			
		}catch(Exception ex){
			return null;
		}		
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static Response getUserById(long id) throws Exception{
		WebTarget baseTarget = DataLayerClient.getWebTarget();
		WebTarget target = baseTarget
				.path("user")
				.path(id+"");
		try{
			return DataLayerClient.fetchGetResponse(target, "application/json");

		}catch(Exception ex){
			return null;
		}		
	}
	
	/**
	 * 
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public static Response getUserByEmail(String email) throws Exception{
		WebTarget baseTarget = DataLayerClient.getWebTarget();
		WebTarget target = baseTarget
				.path("user")
				.path("email")
				.path(email);
		try{
			Response r = DataLayerClient.fetchGetResponse(target, "application/json");
			System.out.println(r);
			return r;
		}catch(Exception ex){
			return null;
		}		
	}
	
}
