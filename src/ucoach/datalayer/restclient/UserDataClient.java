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
			Response response = DataLayerClient.fetchPostResponse(target, "application/json", userJson);
			parseResponseStatus(response);
			return response;
		} catch(Exception ex) {
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
			Response response = DataLayerClient.fetchGetResponse(target, "application/json");
			parseResponseStatus(response);
			return response;
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
		try {
			Response response = DataLayerClient.fetchGetResponse(target, "application/json");
			parseResponseStatus(response);
			return response;
		} catch(Exception ex) {
			return null;
		}
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static Response authorizeUserGoogle(long userId) throws Exception {
		WebTarget baseTarget = DataLayerClient.getWebTarget();
		WebTarget target = baseTarget
				.path("user")
				.path(userId + "")
				.path("google")
				.path("authorization");

		try {
			Response response = DataLayerClient.fetchGetResponse(target, "application/json");
			parseResponseStatus(response);
			return response;
		} catch(Exception ex) {
			return null;
		}
	}
	
	/**
	 * Parse response status
	 * @param response
	 * @throws Exception
	 */
	protected static void parseResponseStatus(Response response) throws Exception {
		// Parse status
		int status = response.getStatus();
		if (status == 500) {
			System.out.println("External Error: response returned " + status);
			System.out.println(response.readEntity(String.class));
			throw new Exception("External Error: response returned " + status);
		}
	}
}
