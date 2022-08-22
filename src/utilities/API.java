package utilities;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.dreambot.api.Client;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.data.ActionMode;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.utilities.impl.Condition;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

import skrept.Main;


public class API {
	
	public static String currentBranch = "";
    public static String currentLeaf = "";
    public static Random rand1 = new Random();
	public static Random rand2 = new Random();
    public static boolean started = false;
    
    
    
   
	public static boolean initialized = false;
	public static double sleepMod;
	
	public static boolean checkedBank()
	{
		//return true;
		if(Bank.getLastBankHistoryCacheTime() <= 0)
		{
			if(Bank.isOpen()) 
			{
				Bank.contains(995);
			}
			else
			{
				if(!Bank.open(Bank.getClosestBankLocation())) Sleep.sleep(666,666);
			}
		}
		if(Bank.getLastBankHistoryCacheTime() > 0)
		{
			return true;
		}
		return false;
	}
	/**
	 * returns true if chatbox is clear
	 * returns false if chatbox has some contents and cleared one
	 * @return
	 */
	public static boolean clearChatWithBackspace()
	{
		String[] x = Widgets.getWidgetChild(162,55).getText().split(":", 2);
    	String[] y = x[1].split("</col>",0);
    	String[] zeChatInputBox = y[0].split("<col=0000ff>",0);
		if(zeChatInputBox != null && zeChatInputBox.length >= 2)
		{
			MethodProvider.log("Clearing chatbox of some contents");
	    	Keyboard.typeSpecialKey(8);
			Sleep.calculate(10,55);
			return false;
		}
		return true;
	}
	public static long cumulativeAFKMillis = 0;
	public static void randomLongerAFK(int chance)
	{
		int roll = API.rand2.nextInt(100);
		boolean moveMouse = false;
		if(API.rand2.nextInt(100) <= chance)
		{
			moveMouse = true;
		}
		if(roll <= chance)
		{
			int tmp = API.rand2.nextInt(100);
			int sleep = 0;
			if(tmp < 2)  
			{
				MethodProvider.log("Longer AFK: 1% chance");
				sleep = Sleep.calculate(10000,160000);
			}
			else if(tmp < 6)  
			{
				MethodProvider.log("Long AFK: 5% chance");
				sleep = Sleep.calculate(10000,80000);
			}
			else if(tmp < 25)
			{
				MethodProvider.log("Longer AFK: 14% chance");
				sleep = Sleep.calculate(10000,60000);
			}
			else if(tmp < 45)  
			{
				MethodProvider.log("Longer AFK: 20% chance");
				sleep = Sleep.calculate(10000,40000);
			}
			else if(tmp < 65)  
			{
				MethodProvider.log("Longer AFK: 20% chance");
				sleep = Sleep.calculate(10000,30000);
			}
			else if(tmp < 1000)  
			{
				MethodProvider.log("Longer AFK: 35% chance");
				sleep = Sleep.calculate(10000,20000);
			}
			Timer sleepTimer = new Timer(sleep);
			cumulativeAFKMillis = cumulativeAFKMillis + sleep;
			final int mean = sleep / 2;
			final int sigma = sleep * 2 / 5;
			Timer mouseMoveTimer = new Timer((int) Calculations.nextGaussianRandom(mean, sigma));
			while(!sleepTimer.finished() && !ScriptManager.getScriptManager().isPaused() &&
					ScriptManager.getScriptManager().isRunning())
			{
				Main.customPaintText2 = "~~ LongerAFK: "+Timer.formatTime(sleepTimer.remaining())+" ~~";
				if(mouseMoveTimer.finished())
				{
					if(Mouse.isMouseInScreen() && 
							Mouse.moveMouseOutsideScreen())
					{
						Sleep.sleep(69,69);
					}
				}
				Sleep.sleep(69, 69);
				
			}
		}
		Main.customPaintText2 = "";
	}
	
	public static Timer runTimer = null;
	public static void randomAFK(int chance)
	{
		int roll = API.rand2.nextInt(100);
		boolean moveMouse = false;
		if(API.rand2.nextInt(100) < chance)
		{
			moveMouse = true;
		}
		if(roll < chance)
		{
			int tmp = API.rand2.nextInt(100);
			int sleep = 0;
			if(tmp < 2)  
			{
				MethodProvider.log("AFK: 1% chance, max 240s");
				sleep = Sleep.calculate(50,84000);
			}
			else if(tmp < 6)  
			{
				MethodProvider.log("AFK: 5% chance, max 120s");
				sleep = Sleep.calculate(50,40000);
			}
			else if(tmp < 25)
			{
				MethodProvider.log("AFK: 14% chance, max 40s");
				sleep = Sleep.calculate(50,20000);
			}
			else if(tmp < 45)  
			{
				MethodProvider.log("AFK: 20% chance, max 20s");
				sleep = Sleep.calculate(50,10000);
			}
			else if(tmp < 65)  
			{
				MethodProvider.log("AFK: 20% chance, max 6.0s");
				sleep = Sleep.calculate(50,4500);
			}
			else if(tmp < 1000)  
			{
				MethodProvider.log("AFK: 35% chance, max 3.2s");
				sleep = Sleep.calculate(50,2400);
			}
			Timer sleepTimer = new Timer(sleep);
			cumulativeAFKMillis = cumulativeAFKMillis + sleep;
			final int mean = sleep / 2;
			final int sigma = sleep * 2 / 5;
			Timer mouseMoveTimer = new Timer((int) Calculations.nextGaussianRandom(mean, sigma));
			while(!sleepTimer.finished() && !ScriptManager.getScriptManager().isPaused() &&
					ScriptManager.getScriptManager().isRunning())
			{
				Main.customPaintText2 = "~~ AFK: "+Timer.formatTime(sleepTimer.remaining())+" ~~";
				if(moveMouse && 
						mouseMoveTimer.finished())
				{
					if(Mouse.isMouseInScreen() && 
							Mouse.moveMouseOutsideScreen())
					{
						Sleep.sleep(69,69);
					}
				}
				Sleep.sleep(69, 69);
				
			}
		}
		Main.customPaintText2 = "";
	}
	public static int roundToMultiple (int number,int multiple){
	    int result = number;
	    //If not already multiple of given number
	    if (number % multiple != 0)
	    {
	        int division = number / multiple;
	        result = division * multiple;
	    }
	    return result;
	}
	public static int getRandomP2PWorld()
	{
		List<World> verifiedWorlds = new ArrayList<World>();
		for(World tmp : Worlds.noMinimumLevel())
		{
			if(	tmp.isMembers()
					&& !tmp.isPVP()
					&& !tmp.isTournamentWorld()
					&& !tmp.isDeadmanMode()
					&& !tmp.isHighRisk() 
					&& !tmp.isLeagueWorld()
					&& !tmp.isSuspicious()
					&& !tmp.isPvpArena() 
					&& !tmp.isTargetWorld()
					&& tmp.getWorld() != 302) //just avoid popular world)
			{
				verifiedWorlds.add(tmp);
			}
		}
		Collections.shuffle(verifiedWorlds);
		return verifiedWorlds.size() > 0 ? verifiedWorlds.get(0).getWorld() : 302; // default world 302 if none found
	}
	
	
	
	
	
	
	
	
	
	
	
}
