import ucoach.businesslogic.rest.control.GoalController;
import ucoach.businesslogic.rest.control.Pretender;

//import ucoach.authentication.restclient.Authenticator;


public  class MainTest {
	
	
	public static void main(String[] args) throws Exception{
		//System.out.println("bananada");
		//Authenticator at = new Authenticator();
		//System.out.println(at.authenticate("abobora"));
		
		GoalController.updateGoals(Pretender.getGoals());
        
		
	}
}
