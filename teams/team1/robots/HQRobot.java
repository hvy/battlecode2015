package team1.robots;


import java.util.HashMap;

import team1.common.Action;
import team1.common.Parameters;
import team1.common.Robot;
import team1.common.SupplyHandler;
import team1.common.Util;
import team1.constants.BroadcastChannel;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class HQRobot extends Robot {
	
	double currentSupplyCount = 0f;
	MapLocation armyCheckPoint;
	MapLocation initialCheckpoint;
	HashMap<Integer, RobotInfo> army;
	
	int armyCount;
	int numSoldiers;
	int numBashers;
	int numBeavers;
	int numBarracks;
	int numMiners;
	int numMinerFactories;
	int numTankfactories;
	int numUnits;
	int numSupplyDepots;
	
	public static double totalSupplyGenerated;
		
	public HQRobot(RobotController rc) {
		super(rc);
		init();
		army = new HashMap<Integer, RobotInfo>();
	}
	
	private void init() {
		// Set the first structure to build
		broadcast.setPreferredStructure(RobotType.MINERFACTORY);
	}
	
	@Override
	public void update() {
		setArmyCheckPoint();
		totalSupplyGenerated = GameConstants.SUPPLY_GEN_BASE
                * (GameConstants.SUPPLY_GEN_MULTIPLIER + Math.pow(numSupplyDepots, GameConstants.SUPPLY_GEN_EXPONENT));
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
		
		if (nearby.length >= 20) {
			
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
	
	@Override
	public void run() throws Exception {
//		int fate = rand.nextInt(10000);
		myRobots = rc.senseNearbyRobots(999999, myTeam);
		
		numSoldiers = 0;
		numBashers = 0;
		numBeavers = 0;
		numBarracks = 0;
		numMiners = 0;
		numMinerFactories = 0;
		numTankfactories = 0;
		numUnits = 0;
		numSupplyDepots = 0;
		
		currentSupplyCount = 0f;
		armyCount = 0;
		for (RobotInfo r : myRobots) {
			currentSupplyCount += r.supplyLevel;
			
			RobotType type = r.type;
			if (type == RobotType.SOLDIER) {
				numSoldiers++;
			} else if (type == RobotType.BASHER) {
				numBashers++;
			} else if (type == RobotType.BEAVER) {
				numBeavers++;
			} else if (type == RobotType.BARRACKS) {
				numBarracks++;
			} else if (type == RobotType.MINER) {
				numMiners++;
			} else if (type == RobotType.MINERFACTORY) {
				numMinerFactories++;
			} else if (type == RobotType.TANKFACTORY) {
				numTankfactories++;
			} else if (type == RobotType.SUPPLYDEPOT) {
				numSupplyDepots++;
			}
			numUnits++;
			
			if (army.containsKey(r.ID))
				armyCount++;
		}
		
		broadcast.sendInt(BroadcastChannel.NUM_BEAVERS, numBeavers);
		broadcast.sendInt(BroadcastChannel.NUM_SOLDIERS, numSoldiers);
		broadcast.sendInt(BroadcastChannel.NUM_BASHERS, numBashers);
		broadcast.sendInt(BroadcastChannel.NUM_MINERS, numMiners);
		broadcast.sendInt(BroadcastChannel.NUM_BARRACKS, numBarracks);
		broadcast.sendInt(BroadcastChannel.NUM_MINER_FACTORIES, numMinerFactories);
		broadcast.sendInt(BroadcastChannel.NUM_TANK_FACTORIES, numTankfactories);
		broadcast.sendInt(BroadcastChannel.NUM_SUPPLY_DEPOTS, numSupplyDepots);
		broadcast.sendInt(BroadcastChannel.NUM_UNITS, numUnits);
		broadcast.sendInt(BroadcastChannel.SUPPLY, (int) currentSupplyCount);
		broadcast.sendLocation(BroadcastChannel.CHECKPOINT, armyCheckPoint);
		//broadcast.sendInt(Parameters.BROAD_CHECKPOINT_Y, armyCheckPoint.y);
		
		// give supply
		SupplyHandler.hqGiveSupply(this);
		
		if (rc.isWeaponReady()) {
			Action.attackSomething(myRange, enemyTeam, rc);
		}

		if (rc.isCoreReady() && rc.getTeamOre() >= 100 && numBeavers < Parameters.MAX_BEAVERS) {
			Action.trySpawn(Util.directions[rand.nextInt(8)], RobotType.BEAVER, rc);
		}
		
	}

	@Override
	public String name() {
		return "HQ";
	}





}