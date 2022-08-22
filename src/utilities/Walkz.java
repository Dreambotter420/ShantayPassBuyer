package utilities;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Shop;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.depositbox.DepositBox;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Map;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.GroundItem;


public class Walkz {
	public static boolean walkPath(Tile[] path)
	{
		List<Tile> pathTiles = new ArrayList<Tile>();
		for(Tile t : path)
		{
			pathTiles.add(t);
		}
		Collections.reverse(pathTiles);
		for(Tile t : pathTiles)
		{
			if(Map.isTileOnMap(t))
			{
				if(Walking.shouldWalk(6) && Walking.walk(t))
				{
					MethodProvider.log("Walked on path(regular walk)!");
					Sleep.sleep(696,420);
				}
				else if(Walking.shouldWalk(6) && Walking.clickTileOnMinimap(t))
				{
					MethodProvider.log("Walked on path (map)!");
					Sleep.sleep(696,420);
				}
				else if(Walking.shouldWalk(6) && Map.interact(t,"Walk here"))
				{
					MethodProvider.log("Walked here on path (screen)!");
					Sleep.sleep(696,420);
				}
				return true;
			}
		}
		MethodProvider.log("No tiles found to path to in path :-(");
		return false;
	}
	
	/**
	 * returns true if have jewelry in invy or equipment. If in invy, equips and then teleports
	 * both to avoid having to handle tele menu interface, and to ensure it teleports in combat.
	 * DOES NOT CHECK BANK FOR JEWELRY! Returns false if no jewelry specified in invy or equipment.
	 * @param jewelry
	 * @param teleName
	 * @return
	 */
	public static boolean useJewelry(int jewelry, String teleName)
	{
		List<Integer> wearableJewelry = new ArrayList<Integer>();
		EquipmentSlot equipSlot = null;
		if(jewelry == InvEquip.wealth) 
			{
			equipSlot = EquipmentSlot.RING;
			wearableJewelry = InvEquip.wearableWealth;
			}
		if(jewelry == InvEquip.glory) 
			{
			equipSlot = EquipmentSlot.AMULET;
			wearableJewelry = InvEquip.wearableGlory;
			}
		if(jewelry == InvEquip.combat) 
			{
			equipSlot = EquipmentSlot.HANDS;
			wearableJewelry = InvEquip.wearableCombats;
			}
		if(jewelry == InvEquip.skills) 
			{
			equipSlot = EquipmentSlot.AMULET;
			wearableJewelry = InvEquip.wearableSkills;
			}
		if(jewelry == InvEquip.games) 
			{
			equipSlot = EquipmentSlot.AMULET;
			wearableJewelry = InvEquip.wearableGames;
			}
		if(jewelry == InvEquip.duel) 
			{
			equipSlot = EquipmentSlot.RING;
			wearableJewelry = InvEquip.wearableDuel;
			}
		if(jewelry == InvEquip.passage) 
			{
			equipSlot = EquipmentSlot.AMULET;
			wearableJewelry = InvEquip.wearablePassages;
			}
		if(InvEquip.equipmentContains(wearableJewelry))
		{
			
			if(Tabs.isOpen(Tab.EQUIPMENT))
			{
				if(Equipment.interact(equipSlot, teleName))
				{
					MethodProvider.log("Just used Jewelry teleport: " + teleName +" in slot: " + equipSlot);
					MethodProvider.sleepUntil(() -> Players.localPlayer().isAnimating(),Sleep.calculate(1111,1111));
					MethodProvider.sleepUntil(() -> !Players.localPlayer().isAnimating(),Sleep.calculate(3333,2222));
					Sleep.sleep(1111,2222);
				}
			}
			else
			{
				if(Shop.isOpen()) Shop.close();
				else if(Bank.isOpen()) Bank.close();
				else if(GrandExchange.isOpen()) GrandExchange.close();
				else if(DepositBox.isOpen()) DepositBox.close();
				else Tabs.open(Tab.EQUIPMENT);
			}
			return true;
		}
		if(InvEquip.invyContains(wearableJewelry))
		{
			final int jewelryID = InvEquip.getInvyItem(wearableJewelry);
			InvEquip.equipItem(jewelryID);
			return true;
		}
		return false;
	}
	
	public static boolean walkToArea(Area area,Tile walkableTile)
	{
		if(area.contains(Players.localPlayer())) return true;
		
		if(Walking.shouldWalk(6))
		{
			if(Walking.walk(walkableTile)) Sleep.sleep(420,696);
		}
		return area.contains(Players.localPlayer());
	}
	public static boolean walkToArea(Area area)
	{
		if(area.contains(Players.localPlayer())) return true;
		
		if(Walking.shouldWalk(6))
		{
			if(Walking.walk(area.getCenter())) Sleep.sleep(420,696);
		}
		return area.contains(Players.localPlayer());
	}
	public static boolean walkToTileInRadius(Tile walkableTile,int radius)
	{
		Area area = walkableTile.getArea(radius);
		
		if(area.contains(Players.localPlayer())) return true;
		if(!Walking.isRunEnabled() &&
				Walking.getRunEnergy() > Sleep.calculate(15, 20)) 
		{
			if(Walking.toggleRun()) Sleep.sleep(69, 111);
		}
		if(Walking.shouldWalk())
		{
			if(Walking.walk(walkableTile)) Sleep.sleep(666, 1111);
		}
		return area.contains(Players.localPlayer());
	}
	public static boolean walkToEntityInArea(String thingName,Area area)
	{
		if(area.contains(Players.localPlayer()))
		{
			//first check NPCs
			NPC npc = NPCs.closest(thingName);
			if(npc != null && npc.exists())
			{
				if(npc.canReach())
				{
					return true;
				}
				else if(Walking.shouldWalk() && Walking.walk(npc.getTile()))
				{
					Sleep.sleep(666, 1111);
				}
				return false;
			}
			//next GameObjects
			GameObject object = GameObjects.closest(thingName);
			if(object != null && object.exists())
			{
				if(object.canReach())
				{
					return true;
				}
				else if(Walking.shouldWalk() && Walking.walk(object.getTile()))
				{
					Sleep.sleep(666, 1111);
				}
				return false;
			}
			//next GroundItems
			GroundItem item = GroundItems.closest(thingName);
			if(item != null && item.exists())
			{
				if(item.canReach())
				{
					return true;
				}
				else if(Walking.shouldWalk() && Walking.walk(item.getTile()))
				{
					Sleep.sleep(666, 1111);
				}
				return false;
			}
		}
		else if(Walking.shouldWalk() && Walking.walk(area.getRandomTile()))
		{
			Sleep.sleep(666, 1111);
		}
		return false;
	}
	public static Timer drunkWalkTimer = null;
	public static int drunkWalksLeft = 0;
	
}
