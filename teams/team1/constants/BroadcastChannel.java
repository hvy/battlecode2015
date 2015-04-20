package team1.constants;

/**
 * Broadcast channels in the range [0, 65535].
 */
public class BroadcastChannel {
	
	// Channels used to broadcast the number of units
	public static int NUM_UNITS 					= 110;
	public static int NUM_BEAVERS 					= 0;
	public static int NUM_SOLDIERS 					= 1;
	public static int NUM_BASHERS 					= 2;
	public static int NUM_MINERS 					= 3;
	public static int NUM_DRONES 					= 4;
	public static int NUM_LAUNCHERS					= 5;
	
	// Channels used to broadcast the number of structures
	public static int NUM_BARRACKS 					= 100;
	public static int NUM_MINER_FACTORIES 			= 101;
	public static int NUM_TANK_FACTORIES 			= 102;
	public static int NUM_SUPPLY_DEPOTS 			= 103;
	public static int NUM_HELIPADS 					= 200;
	
	// Channels used to broadcast the preferred structure to build
	public static int PREFERRED_STRUCTURE 			= 600;
	
	// Channels used to broadcast supply status
	public static int SUPPLY 						= 104;
	public static int SUPPLY_MAX_NEEDED 			= 105;
	public static int CHECKPOINT 					= 120; // covers channel 121 also
	public static int CHECKPOINT2 					= 122; // covers channel 123 also
	public static int MOST_NEEDED_SUPPLY_LOCATION 	= 124; // covers channel 125 also
	
	// Channels used to broadcast missile targets
	public static int MISSILE_BASE = 500;
	
	// Channel used by HQ to broadcast the number of rounds left
	public static int NUM_ROUNDS_LEFT				= 700;
}
