package team1.robots;

import team1.common.Action;
import team1.common.Robot;
import team1.common.Util;
import team1.constants.BroadcastChannel;
import team1.constants.UnitConstants;
import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class HelipadRobot extends Robot {

	public HelipadRobot(RobotController rc) {
		super(rc);
	}

	@Override
	public void update() {

	}
	
	@Override
	public String name() {
		return "Helipad";
	}
	
	@Override
	public void run() throws Exception {
		
		int numDrones, numLaunchers;
		
		if (rc.isCoreReady() && rc.getTeamOre() >= UnitConstants.DRONE_ORE_COST) {
		
			// Spawn the first Drone if there is at least 1 Launcher and
			// spawn the second Drone if there are at least 7 Launchers
			numDrones = broadcast.readInt(BroadcastChannel.NUM_DRONES);
			numLaunchers = broadcast.readInt(BroadcastChannel.NUM_LAUNCHERS);
			
			if((numDrones == 0 && numLaunchers > 1) ||
					(numDrones < 4 && numLaunchers > 7)) {
				Action.trySpawn(Util.getRandomDirection(),RobotType.DRONE, rc);	
			}
		}
	}
}