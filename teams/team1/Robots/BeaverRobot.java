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
			tryToBuildStructure(); 
			
			
			/*if (needMinerFactory()) {
				Action.tryBuild(Util.directions[rand.nextInt(8)],RobotType.MINERFACTORY, rc);
			}*/
			
			/*
			else if (needBarrack()) {
				Action.tryBuild(Util.directions[rand.nextInt(8)],RobotType.MINERFACTORY, rc);
			} else if (needTankFactory()) {
				Action.tryBuild(Util.directions[rand.nextInt(8)],RobotType.TANKFACTORY, rc);
			} else if (needSupplyDepot()) {
				Action.tryBuild(Util.directions[rand.nextInt(8)],RobotType.SUPPLYDEPOT, rc);
			} else if (shouldMine()) {
				rc.mine();
				coreReady = false;
			//} else if (fate < 900) {
			//	Action.tryMove(Util.directions[rand.nextInt(8)], rc);
			} else {
				Action.tryMove(rc.senseHQLocation().directionTo(rc.getLocation()), rc);
			}
			*/
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
		/*
		int numBarracks =  rc.readBroadcast(Parameters.BROAD_NUM_BARRACKS);
		int numMinerFactories = rc.readBroadcast(Parameters.BROAD_NUM_MIN_FACT);
		
		return numBarracks == 0 &&
                rc.getTeamOre() >= StructureConstants.BARRACKS_ORE_COST &&
                numMinerFactories != 0 &&
				Parameters.MAX_BARRACKS > numBarracks;
				*/
		return false;
	}
	
	private boolean needTankFactory() throws GameActionException {
		/*
		int numTankFactories = rc.readBroadcast(Parameters.BROAD_NUM_TANK_FACT);
		int numBarracks =  rc.readBroadcast(Parameters.BROAD_NUM_BARRACKS);
		
		return rc.getTeamOre() >= 500 && 
				numTankFactories < Parameters.MAX_TANK_FACTORIES;
		*/
		
		return false;
	}
	
	private boolean needSupplyDepot() throws GameActionException {
		
		/*
		int numMinerFactories = rc.readBroadcast(Parameters.BROAD_NUM_MIN_FACT);
		int supply = rc.readBroadcast(Parameters.BROAD_SUPPLY);
		int depots = rc.readBroadcast(Parameters.BROAD_NUM_SUPPLY_DEPOTS);
		
		return rc.getTeamOre() >= 100 && 
				supply < 100*(2+Math.pow(depots, 0.6)) &&  
				numMinerFactories != 0;
				*/
		return false;
	}
	
	private boolean shouldMine() throws GameActionException {
		return rc.senseOre(rc.getLocation()) > Parameters.BEAVER_MINE_THRESHOLD;
	}
}
