package ucoach.datalayer.restclient.util;

import javax.ws.rs.client.Invocation.Builder;

import ucoach.businesslogic.rest.manager.ServiceVars;

public class Authorization {

	private static final String AUTHORIZATION_KEY = ServiceVars.DATA_LAYER_AUTHORIZATION_KEY;
	
	/**
	 * Authorize request with authentication key
	 * @param provider
	 */
	public static void authorizeRequest(Builder builder) {
		
		// Get valid authorization key from Environment
    String validAuthKey = AUTHORIZATION_KEY;
    if (String.valueOf(System.getenv("EXTERNALL_DATA_AUTH_KEY")) != "null"){
    	validAuthKey = String.valueOf(System.getenv("EXTERNAL_DATA_AUTH_KEY"));
    }

    builder.header("Authorization", validAuthKey);
	}
}
