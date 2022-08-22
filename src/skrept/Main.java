package skrept;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.dreambot.api.Client;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.Shop;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.grandexchange.LivePrices;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.randoms.RandomManager;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;
import script.paint.CustomPaint;
import script.paint.PaintInfo;
import utilities.API;
import utilities.InvEquip;
import utilities.MissingAPI;
import utilities.Pathz;
import utilities.Sleep;
import utilities.WaitForLogged_N_Loaded;
import utilities.Walkz;




@ScriptManifest(name = "Shantay pass buyer",
description = "Buys shantay passes!", 
author = "Dreambotter420", version = 0.01, category = Category.MISC)
public class Main extends AbstractScript implements PaintInfo
{
	@Override
	public void onStart()
	{
		customPaintText1 = "Starting Buy Shantay Pass script!";
		MethodProvider.log("Starting Buy Shantay Pass Script!");
		Sleep.dt = LocalDateTime.now();
		hoppingBuyTimer = new Timer(60000);
		InvEquip.initializeIntLists();
    	API.rand2.setSeed(LocalTime.now().getNano());
    	Sleep.initSleepMod = 1.2 + (API.rand2.nextDouble()/1.25);
    	Sleep.initSleepMod = Sleep.initSleepMod * Sleep.initSleepMod;
    	API.rand2.setSeed(LocalTime.now().getNano());
    	Sleep.initSleepMod = 1.2 + (API.rand2.nextDouble()/1.25);
    	Sleep.initSleepMod = Sleep.initSleepMod * Sleep.initSleepMod;
    	//all initial randomizations that depend on new random seed go here
    	API.runTimer = new Timer(2000000000);
    	timeSpentHopping = new Timer(1);
    	if(hopsPerHourGoal < 0) hopsPerHourGoal = (int) Calculations.nextGaussianRandom(85,5);
    	
    	bronzePrice = LivePrices.getLow(bronzeBar);
    	passGEPrice = LivePrices.getLow(shantayPass);
    	MethodProvider.log("Initialized");
	}
	@Override
	public void onStart(String[] i)
	{
		MethodProvider.log("OnStart Script quickstart!");
		for(String parameter : i)
		{
			if(parameter == null || parameter.isEmpty()) continue;
			if(parameter.contains("hopsPerHour="))
			{
				hopsPerHourGoal = Integer.parseInt(parameter.split("hopsPerHour=")[1]);
				MethodProvider.log("Set hops per hour goal: " + hopsPerHourGoal);
			}
		}
		onStart();
	}

	public static final int bronzeBar = 2349;
	public static final int shantayPass = 1854;
	public static final Area shantayPassArea = new Area(3310, 3117, 3298, 3131, 0);
	public static int hops = 0;
    public static Timer hoppingBuyTimer = null;
    public static Timer AFKBuyTimer = null;
    public static Timer timeSpentHopping = null;
    public static int initPassCount = -1;
    public static int passCount = 0;
    public static int initBronzeCount = -1;
    public static int bronzeCount = -1;
    public static int bronzePrice = -1;
    public static int passGEPrice = -1;
    public static int hopsPerHourGoal = -1;
	@Override
	public int onLoop() 
	{
		if(WaitForLogged_N_Loaded.isValid())
		{
			customPaintText1 = "~~ Waiting for Logged & Loaded ~~";
			return WaitForLogged_N_Loaded.onLoop();
		}
		if(customPaintText1.contains("Waiting for Logged")) customPaintText1 = "";
		passCount = Inventory.count(shantayPass);
		bronzeCount = Inventory.count(bronzeBar) + Bank.count(bronzeBar);
		if(shantayPassArea.contains(Players.localPlayer()))
		{
			if(!API.checkedBank()) return Sleep.calculate(420, 696);
			if(initBronzeCount < 0) initBronzeCount = Inventory.count(bronzeBar) + Bank.count(bronzeBar);
			if(Inventory.isFull())
			{
				if(Shop.isOpen())
				{
					if(Shop.close())
					{
						MethodProvider.sleepUntil(() ->!Shop.isOpen(),Sleep.calculate(2222, 2222));
					}
					return Sleep.calculate(222, 222);
				}
				if(Bank.isOpen())
				{
					if(Bank.count(InvEquip.coins) < 5000 && Inventory.count(InvEquip.coins) < 2000) 
					{
						MethodProvider.log("Not enough coins (5000) for buying shantay pass for profit!");
						ScriptManager.getScriptManager().stop();
						return Sleep.calculate(111,696);
					}
					if(Bank.depositAllExcept(InvEquip.coins,shantayPass))
					{
						if(Bank.contains(shantayPass))
						{
							final int count = Bank.count(shantayPass);
							if(Bank.withdrawAll(shantayPass))
							{
								MethodProvider.log("Withdrew all ("+count+") shantay passes!");
								MethodProvider.sleepUntil(() -> !Bank.contains(shantayPass), Sleep.calculate(2222, 2222));
							}
							return Sleep.calculate(111,696);
						}
						if(Bank.contains(InvEquip.coins))
						{
							final int count = Bank.count(shantayPass);
							if(Bank.withdrawAll(InvEquip.coins))
							{
								MethodProvider.log("Withdrew all ("+count+") coins!");
								MethodProvider.sleepUntil(() -> !Bank.contains(InvEquip.coins), Sleep.calculate(2222, 2222));
							}
							return Sleep.calculate(111,696);
						}
						if(Bank.close()) return Sleep.calculate(111, 111);
					}
					return Sleep.calculate(111, 696);
				}
				
				if(Bank.open(Bank.getClosestBankLocation())) MethodProvider.sleep(Sleep.calculate(420,696));

				return Sleep.calculate(111,696);
			}
			if(Inventory.count(InvEquip.coins) >= 2000)
			{
				if(initPassCount < 0) initPassCount = Inventory.count(shantayPass);
				if(Bank.isOpen()) 
				{
					if(Bank.close())
					{
						MethodProvider.sleep(Sleep.calculate(420,696));
					}
					return Sleep.calculate(111,696);
				}
				if(Shop.isOpen())
				{
					API.randomAFK(5);
					final int shantayPasses = Shop.count(shantayPass);
					final int bronzeBars = Shop.count(bronzeBar);
					final int hopsPerHour = getHopsPerHour();
					if(hopsPerHour >= hopsPerHourGoal)
					{
						customPaintText1 = "AFK buy mode until hops/hr < "+hopsPerHourGoal;
						if(bronzeBars > 0)
						{
							Filter<Item> filter = i -> i != null && 
									i.getID() == bronzeBar;
							int boughtCount = bronzeBars;
							if(Inventory.emptySlotCount() < boughtCount) boughtCount = Inventory.emptySlotCount();
							if(Shop.interact(filter, "Buy 50"))
							{
								MethodProvider.log("Bought "+boughtCount+" bronze bars from Shantay!");
								MethodProvider.sleep(Sleep.calculate(420,696));
							}
						}
						if(Inventory.isFull()) 
						{
							Shop.close();
							return Sleep.calculate(111, 1111);
						}
						if(shantayPasses >= 450)
						{
							Filter<Item> filter = i -> i != null && 
									i.getID() == shantayPass;
							if(Shop.interact(filter, "Buy 50"))
							{
								if(shantayPasses >= 460)
								{
									Shop.interact(filter, "Buy 50");
								}
								MethodProvider.log("Bought 50 passes from Shantay!");
								API.randomLongerAFK(75);
							}
							return Sleep.calculate(1111,696);
						}
						return Sleep.calculate(1111,696);
					}
					customPaintText1 = "Worldhopping buy mode until hops/hr >= "+hopsPerHourGoal;
					if(bronzeBars > 0)
					{
						Filter<Item> filter = i -> i != null && 
								i.getID() == bronzeBar;
						int boughtCount = bronzeBars;
						if(Inventory.emptySlotCount() < boughtCount) boughtCount = Inventory.emptySlotCount();
						if(Shop.interact(filter, "Buy 50"))
						{
							MethodProvider.log("Bought "+boughtCount+" bronze bars from Shantay!");
							MethodProvider.sleep(Sleep.calculate(420,696));
						}
					}
					if(shantayPasses >= 450)
					{
						Filter<Item> filter = i -> i != null && 
								i.getID() == shantayPass;
						if(Shop.interact(filter, "Buy 50"))
						{
							if(shantayPasses >= 460)
							{
								Shop.interact(filter, "Buy 50");
								final int world = API.getRandomP2PWorld();
								MethodProvider.log("Bought 100 passes from Shantay! Hopping world: " + world);
								MissingAPI.scrollHopWorld(world);
								hops++;
								return Sleep.calculate(111,696);
							}
							MethodProvider.log("Bought 50 passes from Shantay!");
							MethodProvider.sleep(Sleep.calculate(420,696));
						}
						return Sleep.calculate(111,696);
					}

					final int world = API.getRandomP2PWorld();
					MethodProvider.log("See not many shantay passes stocked! Hopping world: " + world);
					MissingAPI.scrollHopWorld(world);
					hops++;
					return Sleep.calculate(111,696);
				}
				
				NPC shantay = NPCs.closest(s -> s != null && 
						s.getName().equals("Shantay") && 
						s.hasAction("Trade"));
				if(shantay == null)
				{
					MethodProvider.log("Shantay null inside shantay pass area! :-(");
					return Sleep.calculate(111,696);
				}
				if(shantay.interact("Trade"))
				{
					MethodProvider.log("Interacted -trade- to -shantay-");
					MethodProvider.sleepUntil(Shop::isOpen, () -> Players.localPlayer().isMoving(),
							Sleep.calculate(2222, 2222),50);
					return Sleep.calculate(111,696);
				}
				
				MethodProvider.sleep(Sleep.calculate(420,696));
				return Sleep.calculate(111,696);
			}
			if(Bank.isOpen())
			{
				if(Bank.count(InvEquip.coins) < 5000) 
				{
					MethodProvider.log("Not enough coins (5000) for buying shantay pass / waterskins to enter desert!");
					ScriptManager.getScriptManager().stop();
					return Sleep.calculate(111,696);
				}
				if(Bank.withdraw(InvEquip.coins, 5000))
				{
					MethodProvider.log("Withdrew 5000 coins!");
					MethodProvider.sleep(Sleep.calculate(420,696));
				}
				return Sleep.calculate(111,696);
			}
			if(Bank.open(Bank.getClosestBankLocation())) MethodProvider.sleep(Sleep.calculate(420,696));
			return Sleep.calculate(111,696);
		}
		
		if(shantayPassArea.getCenter().distance() >= 150)
		{
			if(!Walkz.useJewelry(InvEquip.glory,"Al Kharid"))
			{
				if(!Walkz.useJewelry(InvEquip.duel, "PvP Arena"))
				{
					if(!Walkz.walkPath(Pathz.pathKharidAvoidingScorpion))
					{
						if(!Walkz.walkPath(Pathz.pathKharidAvoidingScorpions2))
						{
							MethodProvider.log("Walking to Al-Karid ... ");
							if(Walking.shouldWalk(6) && Walking.walk(BankLocation.SHANTAY_PASS)) Sleep.sleep(420,696);
						}
					}
				}
			}
			return Sleep.calculate(111,696);
		}
		if(!Walkz.walkPath(Pathz.pathKharidAvoidingScorpion))
		{
			if(!Walkz.walkPath(Pathz.pathKharidAvoidingScorpions2))
			{
				MethodProvider.log("Walking to Al-Karid ... ");
				if(Walking.shouldWalk(6) && Walking.walk(BankLocation.SHANTAY_PASS)) Sleep.sleep(420,696);
			}
		}
		return Sleep.calculate(111,696);
	}
	public static int getHopsPerHour()
	{
		if(API.runTimer == null || hops == 0) return 0;
		final double elapsedMs = (double)API.runTimer.elapsed();
		if(elapsedMs == 0) return 0;
		final double elapsedHours = elapsedMs / 3600000;
		return (int) (hops / elapsedHours);
	}
	public static int getProfitPerHour(boolean gePrice)
	{
		if(API.runTimer == null) return 0;
		double bronzeProfit = 0;
		double passesProfit = 0;
		final double elapsedMs = (double)API.runTimer.elapsed();
		if(elapsedMs == 0) return 0;
		final double elapsedHours = elapsedMs / 3600000;
		final double bronzeBarsGained = (double)(bronzeCount - initBronzeCount);
		if(bronzeBarsGained > 0)
		{
			final double bronzeBarsPerHour = bronzeBarsGained / elapsedHours;
			bronzeProfit = bronzeBarsPerHour * (double)bronzePrice;
		}
		final double passesGained = (double)(passCount - initPassCount);
		if(passesGained > 0)
		{
			final double passesPerHour = passesGained / elapsedHours;
			passesProfit = passesPerHour * (gePrice ? (passGEPrice - 8) : 12);
		}
		
		return API.roundToMultiple((int) (passesProfit + bronzeProfit), 100);
	}
	
    // Our paint info
    // Add new lines to the paint here
    @Override
    public String[] getPaintInfo()
    {
    	return new String[] {
    			getManifest().name() +" "+ getManifest().version() + " by Dreambotter420 ^_^",
                "Total runtime: " + (API.runTimer != null ? Timer.formatTime(API.runTimer.elapsed()) : "N/A"),
                "Hops / hr: " +getHopsPerHour(),
                "Profit: " +(int)((double)getProfitPerHour(true) / 1000)+"k gp/hr",
                customPaintText1,
                customPaintText2,
        };
    }
    public static String customPaintText1 = "";
    public static String customPaintText2 = "";
    public static String customPaintText3 = "";
    public static String customPaintText4 = "";
    // Instantiate the paint object. This can be customized to your liking.
    private final CustomPaint CUSTOM_PAINT = new CustomPaint(this,
            CustomPaint.PaintLocations.BOTTOM_LEFT_PLAY_SCREEN,
            new Color[] {new Color(255, 251, 255)},
            "Trebuchet MS",
            new Color[] {new Color(50, 50, 50, 175)},
            new Color[] {new Color(28, 28, 29)},
            1, false, 5, 3, 0);
    private final RenderingHints aa = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


    @Override
    public void onPaint(Graphics2D graphics2D)
    {
        // Set the rendering hints
        graphics2D.setRenderingHints(aa);
        // Draw the custom paint
        CUSTOM_PAINT.paint(graphics2D);
    }
}