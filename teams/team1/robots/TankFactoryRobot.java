package team1.robots;

import java.util.Random;

import team1.common.Action;
import team1.common.Parameters;
import team1.common.Robot;
import team1.common.Util;
import team1.constants.BroadcastChannel;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;

public class TankFactoryRobot extends Robot {
	
	public TankFactoryRobot(RobotController rc) {
		super(rc);
	}

	@Override
	public void run() throws Exception {
		int fate = rand.nextInt(100);

		// get information broadcasted by the HQ
		int numBeavers = rc.readBroadcast(0);
		int numSoldiers = rc.readBroadcast(1);
		int numBashers = rc.readBroadcast(2);
		int numLaunchers = broadcast.readInt(BroadcastChannel.NUM_LAUNCHERS);
		

		if (rc.isCoreReady()) {
			if (rc.getTeamOre() >= 250 && fate > 10 && numLaunchers > 20) {
				Action.trySpawn(Util.directions[rand.nextInt(8)],RobotType.TANK, rc);
			}
		}
		
	}

	@Override
	public String name() {
		return "Tankfactory";
	}

	@Override
	public void update() {
		coreReady = rc.isCoreReady();
		
	}

}
