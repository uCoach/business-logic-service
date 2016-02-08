
package ucoach.util;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import ucoach.businesslogic.rest.manager.ServiceVars;

public class Authorization {

	private static final String AUTHORIZATION_KEY = ServiceVars.BUSINESS_LOGIC_AUTHORIZATION_KEY;
	
	/**
	 * Method to authenticate key
	 *
	 * @param context
	 * @return
	 */
	public static boolean validateRequest(HttpHeaders headers) {
		if(headers == null)
			return false;
		try{
			List<String> keyList = headers.getRequestHeader("Authorization");
			String authKey = "";
			if(keyList != null){
				authKey = keyList.get(0).toString();
			}
			
			String validAuthKey = AUTHORIZATION_KEY;
			if (String.valueOf(System.getenv("BUSINESS_AUTH_KEY")) != "null"){
				validAuthKey = String.valueOf(System.getenv("BUSINESS_AUTH_KEY"));
			}
			
			if (authKey.equals(validAuthKey)){
				return true;
			}
		
		} catch (Exception e) {e.printStackTrace(); System.out.println("Exception"); }
		
		return false;
	}
}