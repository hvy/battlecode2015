package team1.Robots;

import java.util.Random;

import team1.Action;
import team1.Parameters;
import team1.Robot;
import team1.Util;
import team1.Constants.BroadcastChannel;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;

public class BarracksRobot extends Robot {
	
	public BarracksRobot(RobotController rc) {
		super(rc);
	}

	@Override
	public void run() throws Exception {
		int fate = rand.nextInt(10000);

		// get information broadcasted by the HQ
		//int numBeavers = rc.readBroadcast(Parameters.BROAD_NUM_BEAVERS);
		int numSoldiers = rc.readBroadcast(BroadcastChannel.NUM_SOLDIERS);
		int numBashers = rc.readBroadcast(BroadcastChannel.NUM_BASHERS);
		int numMinFact = rc.readBroadcast(BroadcastChannel.NUM_MINER_FACTORIES);
		int numTankFact = rc.readBroadcast(BroadcastChannel.NUM_TANK_FACTORIES);

		if (rc.isCoreReady() && rc.getTeamOre() >= 60 && Parameters.MAX_BARRACK_UNITS > numSoldiers + numBashers) {
			
//			System.out.println(numSoldiers);
			if (numMinFact == 0)
				return;
			
			if (numTankFact == 0 && numSoldiers > 8)
				return;
			
			if (rc.getTeamOre() > 80 && fate < 100) {
				Action.trySpawn(Util.directions[rand.nextInt(8)],RobotType.BASHER, rc);
			} else {
				Action.trySpawn(Util.directions[rand.nextInt(8)],RobotType.SOLDIER, rc);
			}
		}
		
		
	}

	@Override
	public String name() {
		return "Barracks";
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
