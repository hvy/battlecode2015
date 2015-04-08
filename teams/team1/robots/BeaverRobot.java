package team1.robots;

import team1.common.Action;
import team1.common.Parameters;
import team1.common.Robot;
import team1.common.Util;
import team1.constants.BroadcastChannel;
import team1.constants.StructureConstants;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.TerrainTile;

public class BeaverRobot extends Robot {
	
	private MapLocation buildLocation = null;
	
	public BeaverRobot(RobotController rc) {
		super(rc);
	}
	
	@Override
	public void update() {
	
	}
	
	@Override
	public void run() throws Exception {
		// 1. Attack if there is an enemy nearby.
		if (rc.isWeaponReady()) {
			Action.attackSomething(myRange, enemyTeam, rc);
		}
		
		// 2. Update the current location and find a build location.
		location = rc.getLocation();
		if (buildLocation == null || !mayBuildAt(buildLocation)) {
			updateBuildLocation();
		}
		
		// 3. Try to build the preferred structure if the robot is at a valid build location. Else mine.
		if (location.isAdjacentTo(buildLocation)) {
			boolean successfullyBuilt = tryToBuildAtBuildLocation();
			if (!successfullyBuilt && buildLocation != null && rc.isCoreReady()) {
				System.err.println("MINING!");
				rc.mine();
			}
		} else {
			moveTowardsBuildingLocation();
		}

		/*
		if (needMinerFactory()) {
			Action.tryBuild(Util.directions[rand.nextInt(8)],RobotType.MINERFACTORY, rc);
		} else if (needBarrack()) {
			Action.tryBuild(Util.directions[rand.nextInt(8)],RobotType.BARRACKS, rc);
		} else if (needTankFactory()) {
			Action.tryBuild(Util.directions[rand.nextInt(8)],RobotType.TANKFACTORY, rc);
		} else if (needSupplyDepot()) {
			Action.tryBuild(Util.directions[rand.nextInt(8)],RobotType.SUPPLYDEPOT, rc);
		} else if (shouldMine()) {
			rc.mine();
			coreReady = false;
		} else {
			Action.tryMove(rc.senseHQLocation().directionTo(rc.getLocation()), rc);
		}
		*/
	}

	private boolean mayBuildAt(MapLocation location) {
		
		TerrainTile tile = rc.senseTerrainTile(location);
		
		// Make sure its a valid tile to build on
		if (tile == TerrainTile.VOID || tile == TerrainTile.OFF_MAP) {
			return false;
		}
		
		// Make sure no enemies are nearby
		RobotInfo[] enemies = rc.senseNearbyRobots(location, 15, enemyTeam);
		for (RobotInfo enemy : enemies) {
			if (enemy.type.attackRadiusSquared >= location.distanceSquaredTo(enemy.location)) {
				return false;
			}
		}
		
		return true;
	}
	
	private void updateBuildLocation() {
		
		//Direction direction = location.directionTo(rc.senseHQLocation());
		Direction direction = Util.rndDirection();
		
		int testedTiles = 0;
		int maxTestedTiles = 100;
		
		// Let the check radius increase if no valid tiles are found
		for (int radius = 1;; radius++) {
			
			// Check all 8 neighbor
			for (int quarterPis = 0; quarterPis < 7; quarterPis++) {
				MapLocation candidate = location.add(direction, radius);
				
				if (mayBuildAt(candidate)) {
					//System.err.println("Found valid build coordinate! " + candidate);
					buildLocation = candidate;
					return;
				}
				
				direction = direction.rotateRight();
				
				// Give up after a certain number of tiles
				testedTiles++;
				if(testedTiles >= maxTestedTiles) {
					return;
				}
			}	
		}
	}

	private boolean tryToBuildAtBuildLocation() throws GameActionException {
		
		//Action.tryBuild(Util.directions[rand.nextInt(8)], structureType, rc);
		
		if (!rc.isCoreReady()) {
			return false;
		}
		
		RobotType structureType = broadcast.readPreferredStructure();
		if (structureType == null || structureType == RobotType.HQ) {
			return false;
		} 
		if (structureType.oreCost > rc.getTeamOre()) {
			return false;
		}
		
		if (rc.senseRobotAtLocation(buildLocation) != null) {
			buildLocation = null;
			return false;
		}
		
		Direction buildDirection = location.directionTo(buildLocation);
		if (rc.canBuild(buildDirection, structureType)) {
			System.err.println("fkdjkfdsj;fjsdakfjaskfjsakfsdafjs;");
			rc.build(buildDirection, structureType);
			buildLocation = null;
			return true;
		}
			
		return false;
	}

	private void moveTowardsBuildingLocation() throws GameActionException {
		if (!rc.isCoreReady()) {
			return;
		}
		
		if (location.equals(buildLocation)) {
			moveFromCurrentTile();
		} else {
			Direction moveDirection = location.directionTo(buildLocation);
			Action.tryMove(moveDirection, rc);	
		}
	}
	
	private void moveFromCurrentTile() throws GameActionException {
		
		Direction direction = location.directionTo(rc.senseHQLocation());
		
		// Check all 8 neighbor
		for (int quarterPis = 0; quarterPis < 7; quarterPis++) {
			if (rc.canMove(direction)) {
				rc.move(direction);
				return;
			}
			
			direction = direction.rotateRight();
		}
	}
	
	@Override
	public String name() {
		return "Beaver";
	}
	
	private boolean needMinerFactory() throws GameActionException {
		return rc.getTeamOre() >= StructureConstants.MINER_FACTORY_ORE_COST && 
				broadcast.readInt(BroadcastChannel.NUM_MINER_FACTORIES) < Parameters.MAX_MINER_FACTORIES;
	}
	
	
	private boolean needBarrack() throws GameActionException {
		
		int numBarracks =  broadcast.readInt(BroadcastChannel.NUM_BARRACKS);
		int numMinerFactories = broadcast.readInt(BroadcastChannel.NUM_MINER_FACTORIES);
		
		return numBarracks == 0 &&
                rc.getTeamOre() >= StructureConstants.BARRACKS_ORE_COST &&
                numMinerFactories != 0 &&
				Parameters.MAX_BARRACKS > numBarracks;
				
//		return false;
	}
	
	private boolean needTankFactory() throws GameActionException {
		
//		int numBarracks =  broadcast.readInt(BroadcastChannel.NUM_BARRACKS);
		int numTankFactories = broadcast.readInt(BroadcastChannel.NUM_TANK_FACTORIES);
		
		return rc.getTeamOre() >= 500 && 
				numTankFactories < Parameters.MAX_TANK_FACTORIES;
		
		
//		return false;
	}
	
	private boolean needSupplyDepot() throws GameActionException {
		
		int fate = rand.nextInt(1000);
		
		int numMinerFactories = broadcast.readInt(BroadcastChannel.NUM_MINER_FACTORIES);
		int numBarracks =  broadcast.readInt(BroadcastChannel.NUM_BARRACKS);
		int supply = broadcast.readInt(BroadcastChannel.SUPPLY);
		int depots = broadcast.readInt(BroadcastChannel.NUM_SUPPLY_DEPOTS);
		
		return rc.getTeamOre() >= 100 && fate > 970 &&  
				numMinerFactories != 0 && numBarracks != 0;
				
//		return false;
	}
	
	private boolean shouldMine() throws GameActionException {
		return rc.senseOre(rc.getLocation()) > Parameters.BEAVER_MINE_THRESHOLD;
	}
}
