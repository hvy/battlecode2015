package team1.robots;


import java.util.HashMap;

import team1.common.Action;
import team1.common.Parameters;
import team1.common.Robot;
import team1.common.SupplyHandler;
import team1.common.Util;
import team1.constants.BroadcastChannel;
import team1.constants.StructureConstants;
import team1.constants.UnitConstants;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class HQRobot extends Robot {
	
	double currentSupplyCount = 0f;
	double currentSupplyUpkeep = 0;
	MapLocation armyCheckPoint;
	MapLocation initialCheckpoint;
	HashMap<Integer, RobotInfo> army;
	
	int armyCount;
	int numSoldiers;
	int numBashers;
	int numBeavers;
	int numBarracks;
	int numHelipads;
	int numDrones;
	int numLaunchers;
	int numAerospaceLabs;
	int numMiners;
	int numMinerFactories;
	int numTankfactories;
	int numUnits;
	int numSupplyDepots;
	int numHandwashStations;
	
	public static double totalSupplyGenerated;
		
	public HQRobot(RobotController rc) {
		super(rc);
		army = new HashMap<Integer, RobotInfo>();
	}
	
	@Override
	public String name() {
		return "HQ";
	}

	@Override
	public void update() {
		
	}
	
	@Override
	public void run() throws Exception {

		setArmyCheckPoint();
		
		totalSupplyGenerated = GameConstants.SUPPLY_GEN_BASE
                * (GameConstants.SUPPLY_GEN_MULTIPLIER + Math.pow(numSupplyDepots, GameConstants.SUPPLY_GEN_EXPONENT));
		
		updateRobotCounts();
		broadcastRobotCounts();
		broadcastPreferredStructure();
		
		// give supply
		SupplyHandler.hqGiveSupply(this);
		
		if (rc.isWeaponReady()) {
			Action.attackSomething(myRange, enemyTeam, rc);
		}

		spawnBeaversStrategy();
	}
	
	private void updateRobotCounts() {
		numSoldiers = 0;
		numBashers = 0;
		numBeavers = 0;
		numBarracks = 0;
		numHelipads = 0;
		numMiners = 0;
		numMinerFactories = 0;
		numDrones = 0;
		numLaunchers = 0;
		numTankfactories = 0;
		numUnits = 0;
		numSupplyDepots = 0;
		numAerospaceLabs = 0;
		numHandwashStations = 0;
		
		currentSupplyCount = 0f;
		currentSupplyUpkeep = 0f;
		armyCount = 0;
		
		myRobots = rc.senseNearbyRobots(999999, myTeam);
		
		for (RobotInfo r : myRobots) {
			
			currentSupplyCount += r.supplyLevel;
			currentSupplyUpkeep += r.type.supplyUpkeep;

			RobotType type = r.type;
			if (type == RobotType.SOLDIER) numSoldiers++;
			else if (type == RobotType.BASHER) numBashers++;
			else if (type == RobotType.BEAVER) numBeavers++;
			else if (type == RobotType.BARRACKS) numBarracks++;
			else if (type == RobotType.HELIPAD) numHelipads++;
			else if (type == RobotType.MINER) numMiners++;
			else if (type == RobotType.MINERFACTORY) numMinerFactories++;
			else if (type == RobotType.TANKFACTORY) numTankfactories++;
			else if (type == RobotType.SUPPLYDEPOT) numSupplyDepots++;
			else if (type == RobotType.AEROSPACELAB) numAerospaceLabs++;
			else if (type == RobotType.HANDWASHSTATION) numHandwashStations++; 
			else if (type == RobotType.DRONE) numDrones++;
			else if (type == RobotType.LAUNCHER) numLaunchers++;
			
			numUnits++;
			
			if (army.containsKey(r.ID)) armyCount++;
		}
	}
	
	private void broadcastRobotCounts() throws GameActionException {
		
		broadcast.sendInt(BroadcastChannel.NUM_BEAVERS, numBeavers);
		broadcast.sendInt(BroadcastChannel.NUM_SOLDIERS, numSoldiers);
		broadcast.sendInt(BroadcastChannel.NUM_BASHERS, numBashers);
		broadcast.sendInt(BroadcastChannel.NUM_MINERS, numMiners);
		broadcast.sendInt(BroadcastChannel.NUM_DRONES, numDrones);
		broadcast.sendInt(BroadcastChannel.NUM_BARRACKS, numBarracks);
		broadcast.sendInt(BroadcastChannel.NUM_HELIPADS, numHelipads);
		broadcast.sendInt(BroadcastChannel.NUM_MINER_FACTORIES, numMinerFactories);
		broadcast.sendInt(BroadcastChannel.NUM_TANK_FACTORIES, numTankfactories);
		broadcast.sendInt(BroadcastChannel.NUM_SUPPLY_DEPOTS, numSupplyDepots);
		broadcast.sendInt(BroadcastChannel.NUM_LAUNCHERS, numLaunchers);
		broadcast.sendInt(BroadcastChannel.NUM_UNITS, numUnits);
		broadcast.sendInt(BroadcastChannel.SUPPLY, (int) currentSupplyCount);
		broadcast.sendLocation(BroadcastChannel.CHECKPOINT, armyCheckPoint);
		//broadcast.sendInt(Parameters.BROAD_CHECKPOINT_Y, armyCheckPoint.y);
	}

	/* 
	 * The build order of the structures.
	 */
	private void broadcastPreferredStructure() {
		
		RobotType preferredStructure = RobotType.HQ;
		
		if (numMinerFactories == 0) {
			preferredStructure = RobotType.MINERFACTORY;
		//} else if (numBarracks == 0) {
		//	preferredStructure = RobotType.BARRACKS;
		} else if (numHelipads == 0) {
			preferredStructure = RobotType.HELIPAD;
		} else if (numAerospaceLabs < StructureConstants.AEROSPACE_LAB_MAX) {
			preferredStructure = RobotType.AEROSPACELAB;
		} else {
			int numPreferredSupplyDepots = computeNumPreferredSupplyDepots();
			//System.out.println("Total upkeep: " + currentSupplyUpkeep);
			//System.out.println("Num preferred supply depots: " + numPreferredSupplyDepots);
			if (numPreferredSupplyDepots > numSupplyDepots) {
				preferredStructure = RobotType.SUPPLYDEPOT;
			} 
			
			// Uncomment if Handwash stations are to be built
			// else if (numHandwashStations == 0) {
			//	preferredStructure = RobotType.HANDWASHSTATION;
			// }
		}
		
		broadcast.setPreferredStructure(preferredStructure);
	}

	private int computeNumPreferredSupplyDepots() {
		// Supply generation per turn = 100 * (2 + supply_depots ^ 0.6)
		// Preferably, the supply generation is equal to the current supply upkeep
		
		double numPreferredSupplyDepots = Math.pow((currentSupplyUpkeep / (double) 100) - (double) 2, (double) 10/6);
		
		return (int) (numPreferredSupplyDepots + 0.5);
	}

	private void setArmyCheckPoint() {
		MapLocation[]  enemyTowers = rc.senseEnemyTowerLocations();
		
		if (armyCheckPoint == null) {
			MapLocation[]  towers = rc.senseTowerLocations();
			
			double closest = Double.MAX_VALUE;
			int idx = 0;
			for (int i = 0; i < towers.length; i++) {
				for (int j = 0; j < enemyTowers.length; j++) {
					if (enemyTowers[j].distanceSquaredTo(towers[i]) < closest) {
						closest = enemyTowers[j].distanceSquaredTo(towers[i]);
						idx = i;
					}
				}
			}
			
			if (towers.length > 1)
				armyCheckPoint = towers[idx];
			else
				armyCheckPoint = home;
			initialCheckpoint = armyCheckPoint;
		}
		
		// choose closest tower
		int towerIndex = 0;
		double closestToHome = Double.MAX_VALUE;
		double closestDistance = Double.MAX_VALUE;
		for (int i = 0; i < enemyTowers.length; i++) {
			if (armyCheckPoint.distanceSquaredTo(enemyTowers[i]) < closestDistance) {
				towerIndex = i;
				closestDistance = armyCheckPoint.distanceSquaredTo(enemyTowers[i]);
			}
			
			if (home.distanceSquaredTo(enemyTowers[i]) < closestToHome) {
				closestToHome = home.distanceSquaredTo(enemyTowers[i]);
			}
		}
		
		// if army consists of 10 or more units, start advancing
		RobotInfo[] nearby = rc.senseNearbyRobots(armyCheckPoint, 30, myTeam);
		
		if (nearby.length >= 4) {
			
			army.clear();
			for (int i = 0; i < nearby.length; i++) {
				army.put(nearby[i].ID, nearby[i]);
			}
			
			int x_m = armyCheckPoint.x + (rc.senseEnemyTowerLocations()[towerIndex].x - armyCheckPoint.x)/2;
			int y_m = armyCheckPoint.y - (armyCheckPoint.y - rc.senseEnemyTowerLocations()[towerIndex].y)/2;
			
			armyCheckPoint = new MapLocation(x_m, y_m);
		} else if (armyCount < 6)
			armyCheckPoint = initialCheckpoint;
	}

	private void spawnBeaversStrategy() throws GameActionException {
		if (!rc.isCoreReady()) {
			return;
		}
		
		// Make sure that the first Miner Factory is built before the 3rd Beaver
		if (numBeavers >= 2 && numMinerFactories == 0) {
			return;
		}
		
		if (rc.getTeamOre() >= UnitConstants.BEAVER_ORE_COST && numBeavers < Parameters.MAX_BEAVERS) {
			Action.trySpawn(Util.getRandomDirection(), RobotType.BEAVER, rc);
		}
	}
}
