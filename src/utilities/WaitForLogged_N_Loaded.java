package utilities;


import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;

public class WaitForLogged_N_Loaded 
{
	
    public static boolean isValid() {
        return Client.getGameState() == GameState.LOADING || 
        		Client.getGameState() == GameState.GAME_LOADING || 
        		Client.getGameState() != GameState.LOGGED_IN;
    }
    
    public static int onLoop()
    {
		if(Client.getGameState() == GameState.LOADING || 
        		Client.getGameState() == GameState.GAME_LOADING)
		{
			MethodProvider.sleepUntil(() -> (Client.getGameState() != GameState.LOADING && 
        		Client.getGameState() != GameState.GAME_LOADING), 10000);
			int sleep = (int) Calculations.nextGaussianRandom(1000, 500);
			if(sleep > 0) return sleep;
			else return 1000;
		}
		else
		{
			MethodProvider.sleepUntil(() -> Client.getGameState() == GameState.LOGGED_IN, 7000);
	    	int sleep = (int) Calculations.nextGaussianRandom(1000, 500);
			if(sleep > 0) return sleep;
			else return 1000;
		}
    }

}
