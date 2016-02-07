import ucoach.businesslogic.rest.manager.GoalManager;
import ucoach.businesslogic.rest.manager.JSONBuilder;
import ucoach.businesslogic.rest.manager.Pretender;
import ucoach.util.DatePattern;

//import ucoach.authentication.restclient.Authenticator;


public  class MainTest {
	
	
	public static void main(String[] args) throws Exception{
		//System.out.println("bananada");
		//Authenticator at = new Authenticator();
		//System.out.println(at.authenticate("abobora"));
		
		//GoalController.updateGoals(Pretender.getGoals());
        //System.out.println(JSONBuilder.singleJsonResponse(Pretender.getUser(), Pretender.getSingleHealthMeasure(),"nomedocampo" ));
		System.out.println(DatePattern.dateFormater(DatePattern.getYesterdayDate()));
	}
}
