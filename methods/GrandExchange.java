package org.powerbot.game.api.methods;

import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

/**
 * In-game interaction with GE
 * 
 * @author (Brad/Javaskill)
 */
public class GrandExchange {
	
	public static final int WIDGET_GE = 105;
	
	public static final int[] WIDGET_GE_SLOTS = { 19, 35, 51, 70, 89, 108 };
	public static final int[] WIDGET_GE_SELL_SLOT_BUTTON = { 29, 45, 61, 80, 99, 118 };
	public static final int[] WIDGET_GE_BUY_SLOT_BUTTON = { 30, 46, 62, 81, 100, 119 };
	public static final int[] WIDGET_GE_BUY_QUANTITY = { 160, 162, 164, 166, 168 };
	
	public static final int WIDGET_GE_SLOT_EMPTY = 10;
	public static final int WIDGET_GE_SLOT_TYPE = 134;
	public static final int WIDGET_GE_SLOT_BACK_BUTTON = 128;
	public static final int WIDGET_GE_BUY_BUTTON = 137;
	public static final int WIDGET_GE_PRICE_BUTTON = 177;
	public static final int WIDGET_GE_CONFIRM_BUTTON = 187;
	public static final int WIDGET_GE_ITEM_SELECTION = 389;
	public static final int WIDGET_GE_ITEM_SELECTION_LIST = 4;
	public static final int WIDGET_GE_ITEM_NAME = 142;
	public static final int WIDGET_GE_ITEM_QUANTITY = 148;
	public static final int WIDGET_GE_ITEM_PRICE = 153;
	public static final int WIDGET_GE_ITEM_TOTAL_PRICE = 185;
	public static final int WIDGET_GE_CLOSE = 14;
	public static final int WIDGET_GE_OFFER_ABORT = 200;
	public static final int WIDGET_GE_5_PERCENT_UP = 179;
	public static final int WIDGET_GE_5_PERCENT_DOWN = 181;
	public static final int WIDGET_GE_OFFER_MARKET_PRICE = 175;
	
	public static final Tile NORTH_WEST = new Tile(3143, 3510, 0);
	public static final Tile SOUTH_EAST = new Tile(3185, 3471, 0);
	public static final Area GEAREA = new Area(NORTH_WEST, SOUTH_EAST);
	
	public static final int[] GRAND_EXCHANGE_CLERKS = { 2593, 1419, 2240, 2241 };
	public static final String OPEN_ACTION = "Exchange";
	
	/**
	 * Buys the specified item in the GE<br>
	 * Player must be at the GE<br>
	 * The price of 0 = Market Price
	 * 
	 * @param name The <B>Exact</b> Name of the item, case ignored
	 * @param price The buying price (0 = market)
	 * @param amount The amount to buy
	 * @return True on success
	 */
	public static boolean buy(final String name, int price, final int amount){
		if(!isOpen()){
			if(!open()){
				return false; // Failed to open the GE
			}
		}
		sleep(750,900);
		int slot = getFreeSlot();
		if(slot == -1)
			return false; // No free slots
		if(!openBuySlot(slot))
			return false;
		sleep(900,1200);
		if(!getItemName().equalsIgnoreCase(name))
		if(!chooseBuyingItem(name))
			return false;
		sleep(800, 1100);
		if(getCurrentQuantity() != amount)
		if(!setQuantity(amount))
			return false;
		sleep(750,1250);
		if(price == 0)
			price = getCurrentPrice();
		else
		if(getCurrentPrice() != price)
		if(!setPrice(price))
			return false;
		sleep(800,1200);
		if(getOfferPrice() == amount*price)
		return confirm();
		pressBack();
		return false;
	}
	/**
	 * Buys an item and presses the 5 percent up button the specified amount of times<br>
	 * Player must be at GE<br>
	 * 
	 * @param name The <b> Exact </b> Name of the item, case ignored
	 * @param amount The amount to sell
	 * @param upButtonCount The number of times to press the 5 percent up button
	 * @return
	 */
	public static boolean buyUp(final String name, final int amount, final int upButtonCount){
		if(!isOpen()){
			if(!open()){
				return false; // Failed to open the GE
			}
		}
		sleep(750,900);
		int slot = getFreeSlot();
		if(slot == -1)
			return false; // No free slots
		if(!openBuySlot(slot))
			return false;
		sleep(900,1200);
		if(!getItemName().equalsIgnoreCase(name))
			if(!chooseBuyingItem(name))
				return false;
		sleep(800, 1100);
		if(getCurrentQuantity() != amount)
			if(!setQuantity(amount))
				return false;
		sleep(750,1250);
		for(int i = 0;i < upButtonCount;i++){
			ficePercentUp();
		}
		final int price = getCurrentPrice();
		if(getOfferPrice() == amount*price)
		return confirm();
		pressBack();
		return false;
	}
	/**
	 * Buys an item and presses the 5 percent down button the specified amount of times<br>
	 * Player must be at GE<br>
	 * 
	 * @param name The <b> Exact </b> Name of the item, case ignored
	 * @param amount The amount to sell
	 * @param downButtonCount The number of times to press the 5 percent down button
	 * @return
	 */
	public static boolean buyDown(final String name, final int amount, final int downButtonCount){
		if(!isOpen()){
			if(!open()){
				return false; // Failed to open the GE
			}
		}
		sleep(750,900);
		int slot = getFreeSlot();
		if(slot == -1)
			return false; // No free slots
		if(!openBuySlot(slot))
			return false;
		sleep(900,1200);
		if(!getItemName().equalsIgnoreCase(name))
			if(!chooseBuyingItem(name))
				return false;
		sleep(800, 1100);
		if(getCurrentQuantity() != amount)
			if(!setQuantity(amount))
				return false;
		sleep(750,1250);
		for(int i = 0;i < downButtonCount;i++){
			ficePercentDown();
		}
		final int price = getCurrentPrice();
		if(getOfferPrice() == amount*price)
		return confirm();
		pressBack();
		return false;
	}
	//
	/**
	 * Sells the specified item in the GE. <br>
	 * Player MUST be at the GE. Derp <br>
	 * The item must be in the Inventory <br>
	 * The price of 0 = market price
	 * 
	 * @param id The ID of the item
	 * @param price The selling price Set to 0 for the market price
	 * @param amount The amount to sell
	 * @return True on success
	 */
	public static boolean sell(final int id, int price, final int amount){
		if(!isOpen()){
			if(!open()){
				return false; // Failed to open the GE
			}
		}
		sleep(750,900);
		int slot = getFreeSlot();
		if(slot == -1)
			return false; // No free slots
		if(!openSellSlot(slot))
			return false;
		sleep(900,1200);
		Item item = Inventory.getItem(id);
		if(!getItemName().equalsIgnoreCase(item.getName())){
			if(!item.getWidgetChild().click(true))
				return false;
		}
		sleep(800, 1100);
		if(getCurrentQuantity() != amount)
		if(!setQuantity(amount))
			return false;
		sleep(750,1250);
		if(price == 0)
			price = getCurrentPrice();
		else
		if(getCurrentPrice() != price)
		if(!setPrice(price))
			return false;
		sleep(800,1200);
		if(getOfferPrice() == amount*price)
		return confirm();
		pressBack();
		return false;
	}
	/**
	 * Sells an item and presses the 5 percent down button the specified amount of times<br>
	 * Player must be at GE<br>
	 * Item must be in the Inventory
	 * 
	 * @param id The ID of the item
	 * @param amount The amount to sell
	 * @param downButtonCount The number of times to press the 5 percent down button
	 * @return
	 */
	public static boolean sellDown(final int id, final int amount, final int downButtonCount){
		if(!isOpen()){
			if(!open()){
				return false; // Failed to open the GE
			}
		}
		sleep(750,900);
		int slot = getFreeSlot();
		if(slot == -1)
			return false; // No free slots
		if(!openSellSlot(slot))
			return false;
		sleep(900,1200);
		Item item = Inventory.getItem(id);
		if(!getItemName().equalsIgnoreCase(item.getName())){
			if(!item.getWidgetChild().click(true))
				return false;
		}
		sleep(800, 1100);
		if(getCurrentQuantity() != amount)
		if(!setQuantity(amount))
			return false;
		sleep(750,1250);
		for(int i = 0;i < downButtonCount;i++){
			ficePercentDown();
		}
		final int price = getCurrentPrice();
		if(getOfferPrice() == amount*price)
		return confirm();
		pressBack();
		return false;
	}
	/**
	 * Sells an item and presses the 5 percent up button the specified amount of times<br>
	 * Player must be at GE<br>
	 * Item must be in the inventory
	 * 
	 * @param id The ID of the item
	 * @param amount The amount to sell
	 * @param upbuttoncount The number of times to press the 5 percent up button
	 * @return
	 */
	public static boolean sellUp(final int id, final int amount, final int upButtonCount){
		if(!isOpen()){
			if(!open()){
				return false; // Failed to open the GE
			}
		}
		sleep(750,900);
		int slot = getFreeSlot();
		if(slot == -1)
			return false; // No free slots
		if(!openSellSlot(slot))
			return false;
		sleep(900,1200);
		Item item = Inventory.getItem(id);
		if(!getItemName().equalsIgnoreCase(item.getName())){
			if(!item.getWidgetChild().click(true))
				return false;
		}
		sleep(800, 1100);
		if(getCurrentQuantity() != amount)
		if(!setQuantity(amount))
			return false;
		sleep(750,1250);
		for(int i = 0;i < upButtonCount;i++){
			ficePercentUp();
		}
		final int price = getCurrentPrice();
		if(getOfferPrice() == amount*price)
		return confirm();
		pressBack();
		return false;
	}
	private static void ficePercentUp(){
		getWidget().getChild(WIDGET_GE_5_PERCENT_UP).click(true);
		sleep(150,350);
	}
	private static void ficePercentDown(){
		getWidget().getChild(WIDGET_GE_5_PERCENT_DOWN).click(true);
		sleep(150,350);
	}
	/**
	 * Abborts the offer at the specific slot
	 * 
	 * @param slot The slot (1-6)
	 */
	public static void abbortOffer(final int slot){
		if(!isOpen())
			if(!open())
				return;
		if(getSlot(slot).click(true)){
			sleep(1200,1400);
			getWidget().getChild(WIDGET_GE_OFFER_ABORT).click(true);
			sleep(1400,1600);
			WidgetChild coinslot = getWidget().getChild(206);
			if(coinslot.getChildId() != -1){
				coinslot.click(true);
				sleep(500,750);
			}
			WidgetChild itemslot = getWidget().getChild(208);
			if(itemslot.getChildId() != -1){
				itemslot.click(true);
				sleep(500,750);
			}
		}
	}
	private static int getOfferPrice(){
		return Integer.parseInt(getWidget().getChild(WIDGET_GE_ITEM_TOTAL_PRICE).getText().replaceAll(",", "").split(" ")[0]);
	}
	private static int getCurrentPrice(){
		return Integer.parseInt(getWidget().getChild(WIDGET_GE_ITEM_PRICE).getText().replaceAll(",", "").split(" ")[0]);
	}
	private static int getCurrentQuantity(){
		return Integer.parseInt(getWidget().getChild(WIDGET_GE_ITEM_QUANTITY).getText());
	}
	private static String getItemName(){
		if(isOpen())
		return getWidget().getChild(WIDGET_GE_ITEM_NAME).getText();
		return null;
	}
	private static boolean chooseBuyingItem(String item) {
		if (!getWidget().getChild(142).getText().equalsIgnoreCase(item)) {
			sleep(1250,1650);
			Keyboard.sendText(item, false);
			sleep(1000, 1450);
			return Mouse.click(65+Random.nextInt(0, 250), 345+Random.nextInt(0, 9), true);
		}
		return false;
	}
	private static boolean confirm(){
		if(getWidget().getChild(WIDGET_GE_CONFIRM_BUTTON).click(true)){
			sleep(650,750);
			return true;
		}
		return false;
	}
	private static boolean setPrice(final int price){
		if(getWidget().getChild(WIDGET_GE_PRICE_BUTTON).click(true)){
			sleep(900,1200);
			Keyboard.sendText(""+price, true);
			sleep(750,900);
			return true;
		}
		return false;
	}
	private static boolean setQuantity(final int amount){
		switch(amount){
		case 1:
			return getWidget().getChild(WIDGET_GE_BUY_QUANTITY[0]).click(true);
		case 10:
			return getWidget().getChild(WIDGET_GE_BUY_QUANTITY[1]).click(true);
		case 100:
			return getWidget().getChild(WIDGET_GE_BUY_QUANTITY[2]).click(true);
		case 1000:
			return getWidget().getChild(WIDGET_GE_BUY_QUANTITY[3]).click(true);
		default:
			if(getWidget().getChild(WIDGET_GE_BUY_QUANTITY[4]).click(true)){
				sleep(750,900);
				Keyboard.sendText(""+amount, true);
				sleep(350,500);
				return true;
			}
			return false;
		}
	}
	/**
	 * The clerk needs to be in the loaded region for this to work
	 * 
	 * @return True on success
	 */
	public static boolean open(){
		if(isOpen())
			return true;
		if(isAtGE()){
			NPC clerk = NPCs.getNearest(GRAND_EXCHANGE_CLERKS);
			if(clerk != null){
				if(!clerk.isOnScreen()){
					Walking.walk(clerk.getLocation());
					sleep(450,650);
					while(Players.getLocal().isMoving()){
						sleep(150,350);
					}
				}
				if(clerk.isOnScreen()){
					clerk.interact(OPEN_ACTION);
					sleep(750,1250);
					while(Players.getLocal().isMoving()){
						sleep(450,850);
					}
					return isOpen();
				} else {
					return false;
				}
			}
			return false;
		} else {
			System.out.println("Not at GE!");
			return false;
		}
	}
	private static void sleep(final int min, final int max){
		try {
			Thread.sleep(Random.nextInt(min, max));
		} catch(Exception e){}
	}
	/**
	 * Checks if the current player is at the GE
	 * 
	 * @return True if the player is at the GE
	 */
	public static boolean isAtGE(){
		return GEAREA.contains(Players.getLocal().getLocation());
	}
	/**
	 * 
	 * @return 0 for Buy inferface, 1 for Sell interface and 2 for unknown
	 */
	private static int getInterfaceType(){
		int type = 2;
		if(isOpen()){
			String value = getWidget().getChild(WIDGET_GE_SLOT_TYPE).getText();
			if(value.equals("Buy Offer")){
				type = 0;
			} else if(value.endsWith("Sell Offer")){
				type = 1;
			}
		}
		return type;
	}
	/**
	 * Closes the GE if it's currently open
	 */
	public static void close(){
		if(isOpen()){
			getWidget().getChild(WIDGET_GE_CLOSE).click(true);
		}
	}
	/**
	 * Opens the specified slot
	 * 
	 * @param slot The slot number (1-6)
	 * @return True on success
	 */
	public static boolean openBuySlot(final int slot){
		if(isOpen()){
			if(getInterfaceType() == 1){
				pressBack();
			}
			return getWidget().getChild(WIDGET_GE_BUY_SLOT_BUTTON[slot-1]).click(true);
		}
		return false;
	}
	/**
	 * Opens the specified slot
	 * 
	 * @param slot The slot number (1-6)
	 * @return True on success
	 */
	public static boolean openSellSlot(final int slot){
		if(isOpen()){
			if(getInterfaceType() == 0){
				pressBack();
			}
			return getWidget().getChild(WIDGET_GE_SELL_SLOT_BUTTON[slot-1]).click(true);
		}
		return false;
	}
	/**
	 * Presses the back button to go to the main GE interface
	 * 
	 * @return True on success
	 */
	private static boolean pressBack(){
		if(isOpen()){
			return getWidget().getChild(WIDGET_GE_SLOT_BACK_BUTTON).click(true);
		}
		return false;
	}
	/**
	 * Checks if the Grand Exhcange is open
	 * 
	 * @return True if the GE interface is open
	 */
	public static boolean isOpen(){
		return getWidget().validate();
	}
	/**
	 * Finds an empty slot in the GE. Only works if the GE is open
	 * 
	 * @return The slot number(1-6) or -1 if no slot is found
	 */
	public static int getFreeSlot(){
		if(isOpen())
		for(int slot = 1; slot < 6;slot++){
			if(isSlotEmpty(slot)){
				return slot;
			}
		}
		return -1;
	}
	/**
	 * Checks if the slot is empty. Only works if the GE is open
	 * 
	 * @param slot The slot number (1-6)
	 * @return True if the specified slot is not being used
	 */
	public static boolean isSlotEmpty(final int slot){
		if(slot >= 1 && slot <= 6){
			return getWidget().getChild(WIDGET_GE_SLOTS[slot-1]).getChild(WIDGET_GE_SLOT_EMPTY).getText().toLowerCase().equals("empty");
		}
		return false;
	}
	/**
	 * Gets the WidgetChild if the GE is open
	 * 
	 * @param slot The slot number (1-6)
	 * @return The slot WidgetChild of the Ge Widget
	 */
	public static WidgetChild getSlot(final int slot){
		if(slot >= 1 && slot <= 6){
			return getWidget().getChild(WIDGET_GE_SLOTS[slot-1]);
		}
		return null; // Illegal Arguments
	}
	/**
	 * 
	 * @return The GE widget
	 */
	public static Widget getWidget(){
		return Widgets.get(WIDGET_GE);
	}
}