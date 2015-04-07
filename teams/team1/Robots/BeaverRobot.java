package team1.robots;

import team1.common.Action;
import team1.common.Parameters;
import team1.common.Robot;
import team1.common.Util;
import team1.constants.BroadcastChannel;
import team1.constants.StructureConstants;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class BeaverRobot extends Robot {
	
	public BeaverRobot(RobotController rc) {
		super(rc);
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	public void run() throws Exception {
		if (rc.isWeaponReady()) {
			Action.attackSomething(myRange, enemyTeam, rc);
		}
		
		if (rc.isCoreReady()) {
//			tryToBuildStructure(); 
			
			
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
			
			
		}
		
	}

	private void tryToBuildStructure() throws GameActionException {
		RobotType structureType = broadcast.readPreferredStructure();
		if (structureType != null) {
			Action.tryBuild(Util.directions[rand.nextInt(8)], structureType, rc);	
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
