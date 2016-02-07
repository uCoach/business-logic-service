import org.json.JSONObject;

import ucoach.businesslogic.rest.manager.GoalManager;
import ucoach.businesslogic.rest.manager.JSONBuilder;
import ucoach.businesslogic.rest.manager.Pretender;
import ucoach.datalayer.restclient.UserDataClient;
import ucoach.util.DatePatterns;

//import ucoach.authentication.restclient.Authenticator;


public  class MainTest {
	
	
	public static void main(String[] args) throws Exception{
		//System.out.println("bananada");
		//Authenticator at = new Authenticator();
		//System.out.println(at.authenticate("abobora"));
		
		//GoalController.updateGoals(Pretender.getGoals());
        //System.out.println(JSONBuilder.singleJsonResponse(Pretender.getUser(), Pretender.getSingleHealthMeasure(),"nomedocampo" ));
		//System.out.println(DatePattern.dateFormater(DatePattern.getYesterdayDate()));
		//System.out.println(UserDataClient.getUserById(1));
		//System.out.println(UserDataClient.getUserById(2));
		//System.out.println(UserDataClient.getUserById(9999));
		
		String json = "{"+
				  "\"firstname\": \"John\","+
				  "\"lastname\": \"Snow\","+
				  "\"email\": \"johnjohn@mawril.co\","+
				  "\"birthdate\": \"1990-1-1\","+
				  "\"password\": \"encryptedPassword\", "+
				  "\"twitterUsername\": \"anidi\" "+
				  "}";
		JSONObject obj;
		obj = new JSONObject(json);
		System.out.println(UserDataClient.registerUser(obj));
	}
}
