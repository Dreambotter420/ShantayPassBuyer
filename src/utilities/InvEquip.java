package utilities;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.Shop;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.container.impl.bank.BankMode;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.depositbox.DepositBox;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.grandexchange.LivePrices;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.items.Item;


public class InvEquip {
	
	public static int getFirstInBank(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Bank.contains(i)) return i;
		}
		return -1;
	}
	public static int getFirstInEquipment(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Equipment.contains(i)) return i;
		}
		return -1;
	}
	public static int getFirstInInventory(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Inventory.contains(i)) return i;
		}
		return -1;
	}
	public static boolean invyContains(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Inventory.contains(i)) return true;
		}
		return false;
	}
	public static int getInvyItem(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Inventory.contains(i)) return i;
		}
		return 0;
	}
	public static boolean equipmentContains(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Equipment.contains(i)) return true;
		}
		return false;
	}
	public static int getEquipmentItem(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Equipment.contains(i)) return i;
		}
		return 0;
	}
	public static int getBankItem(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Bank.contains(i)) return i;
		}
		return 0;
	}
	public static boolean bankContains(List<Integer> ints)
	{
		for(int i: ints)
		{
			if(Bank.contains(i)) return true;
		}
		return false;
	}
	
	public static boolean equipItem(int ID)
	{

		if(GrandExchange.isOpen()) 
		{
			GrandExchange.close();
			return false;
		}
		if(Equipment.contains(ID)) return true;
		if(!Inventory.contains(ID)) return false;
		MethodProvider.log("Equipping item: " + new Item(ID,1).getName());
		if(Tabs.isOpen(Tab.INVENTORY) || Bank.isOpen() ||
				(Widgets.getWidgetChild(12, 76) != null && Widgets.getWidgetChild(12, 76).isVisible()))
		{
			Item wearItem = Inventory.get(ID);
			if(wearItem == null) return false;
			String action = null;
			for(String act : wearItem.getActions())
			{
				if(act == null) continue;
				if(act.equals("Wield")) 
				{
					action = "Wield";
					break;
				}
				else if(act.equals("Wear")) 
				{
					action = "Wear";
					break;
				}
			}
			final int tmp = ID;
			if(Inventory.interact(ID, action))
			{
				MethodProvider.sleepUntil(() -> Equipment.contains(tmp), Sleep.calculate(2222, 2222));
			}
			if(Equipment.contains(ID)) return true;
			return false;
		}
		else
		{
			if(Shop.isOpen()) Shop.close();
			else if(Bank.isOpen()) Bank.close();
			else if(DepositBox.isOpen()) DepositBox.close();
			else Tabs.open(Tab.INVENTORY);
		}
		return false;
	}
	
	
	
	
	
	public static int coins = 995;
	
	public static void initializeIntLists ()
	{
		wearablePassages.add(passage1);wearablePassages.add(passage2);
		wearablePassages.add(passage3);wearablePassages.add(passage4);wearablePassages.add(passage5);
		
		wearableGames.add(games1);wearableGames.add(games2);wearableGames.add(games3);wearableGames.add(games4);
		wearableGames.add(games5);wearableGames.add(games6);wearableGames.add(games7);wearableGames.add(games8);
		
		wearableDuel.add(duel1);wearableDuel.add(duel2);wearableDuel.add(duel3);wearableDuel.add(duel4);
		wearableDuel.add(duel5);wearableDuel.add(duel6);wearableDuel.add(duel7);wearableDuel.add(duel8);
		
		wearableSkills.add(skills1);wearableSkills.add(skills2);wearableSkills.add(skills3);
		wearableSkills.add(skills4);wearableSkills.add(skills5);wearableSkills.add(skills6);

		wearableGlory.add(glory1);wearableGlory.add(glory2);wearableGlory.add(glory3);
		wearableGlory.add(glory4);wearableGlory.add(glory5);wearableGlory.add(glory6);
		
		wearableWealth.add(wealth1);wearableWealth.add(wealth2);
		wearableWealth.add(wealth3);wearableWealth.add(wealth4);wearableWealth.add(wealth5);
		
		wearableCombats.add(combat1);wearableCombats.add(combat2);wearableCombats.add(combat3);
		wearableCombats.add(combat4);wearableCombats.add(combat5);wearableCombats.add(combat6);
		
		for(int jewelry : wearablePassages)
		{
			allJewelry.put(EquipmentSlot.AMULET, jewelry);
		}
		for(int jewelry : wearableGames)
		{
			allJewelry.put(EquipmentSlot.AMULET, jewelry);
		}
		for(int jewelry : wearableSkills)
		{
			allJewelry.put(EquipmentSlot.AMULET, jewelry);
		}
		for(int jewelry : wearableGlory)
		{
			allJewelry.put(EquipmentSlot.AMULET, jewelry);
		}
		for(int jewelry : wearableCombats)
		{
			allJewelry.put(EquipmentSlot.HANDS, jewelry);
		}
		for(int jewelry : wearableDuel)
		{
			allJewelry.put(EquipmentSlot.RING, jewelry);
		}
		for(int jewelry : wearableWealth)
		{
			allJewelry.put(EquipmentSlot.AMULET, jewelry);
		}
		allJewelryIDs.addAll(wearableWealth);
		allJewelryIDs.addAll(wearablePassages);
		allJewelryIDs.addAll(wearableGames);
		allJewelryIDs.addAll(wearableSkills);
		allJewelryIDs.addAll(wearableGlory);
		allJewelryIDs.addAll(wearableCombats);
		allJewelryIDs.addAll(wearableDuel);
		
	}
	
	public static List<Integer> allJewelryIDs = new ArrayList<Integer>();
	public static int jewelry = -10;

	public static Map<EquipmentSlot,Integer> allJewelry = new LinkedHashMap<EquipmentSlot,Integer>();
	//-8 represents the value of any charge of combat bracelet
	public static int combat = -8; 
	public static List<Integer> wearableCombats = new ArrayList<Integer>();
	public static int combat1 = 11124;
	public static int combat2 = 11122;
	public static int combat3 = 11120;
	public static int combat4 = 11118;
	public static int combat5 = 11974;
	public static int combat6 = 11972;
		
	//-7 represents the value of any charge of necklace of glory
	public static int passage = -7; 
	public static List<Integer> wearablePassages = new ArrayList<Integer>();
	public static int passage1 = 21155;
	public static int passage2 = 21153;
	public static int passage3 = 21151;
	public static int passage4 = 21149;
	public static int passage5 = 21146;
	
	//-6 represents the value of any charge of games necklace
	public static int games = -6; 
	public static List<Integer> wearableGames = new ArrayList<Integer>();
	public static int games1 = 3867;
	public static int games2 = 3865;
	public static int games3 = 3863;
	public static int games4 = 3861;
	public static int games5 = 3859;
	public static int games6 = 3857;
	public static int games7 = 3855;
	public static int games8 = 3853;
	
	//-5 represents the value of any charge of dueling ring
	public static int duel = -5; 
	public static List<Integer> wearableDuel = new ArrayList<Integer>();
	public static int duel1 = 2566;
	public static int duel2 = 2564;
	public static int duel3 = 2562;
	public static int duel4 = 2560;
	public static int duel5 = 2558;
	public static int duel6 = 2556;
	public static int duel7 = 2554;
	public static int duel8 = 2552;
	
	//-4 represents the value of any charge of skills necklace
	public static int skills = -4; 
	public static List<Integer> wearableSkills = new ArrayList<Integer>();
	public static int skills0 = 11113;
	public static int skills1 = 11111;
	public static int skills2 = 11109;
	public static int skills3 = 11107;
	public static int skills4 = 11105;
	public static int skills5 = 11970;
	public static int skills6 = 11968;
	
	//-3 represents the value of any charge of glory
	public static int glory = -3; 
	public static List<Integer> wearableGlory = new ArrayList<Integer>();
	public static int glory0 = 1704;
	public static int glory1 = 1706;
	public static int glory2 = 1708;
	public static int glory3 = 1710;
	public static int glory4 = 1712;
	public static int glory5 = 11976;
	public static int glory6 = 11978;
	
	//-2 represents the value of any charge of wealth
	public static int wealth = -2; 
	public static List<Integer> wearableWealth = new ArrayList<Integer>();
	public static int wealth0 = 2572;
	public static int wealth1 = 11988;
	public static int wealth2 = 11986;
	public static int wealth3 = 11984;
	public static int wealth4 = 11982;
	public static int wealth5 = 11980;
}
