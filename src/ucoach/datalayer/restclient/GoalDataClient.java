package ucoach.datalayer.restclient;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import ucoach.datalayer.restclient.util.DataLayerClient;

public class GoalDataClient {
	
	
	public static Response registerGoal(JSONObject goalJson) throws Exception{
		WebTarget baseTarget = DataLayerClient.getWebTarget();
		WebTarget target = baseTarget
				.path("goal");
		try{
			Response r = DataLayerClient.fetchPostResponse(target, "application/json", goalJson);
			return r;
		}catch(Exception ex){
			return null;
		}		
	}
}
