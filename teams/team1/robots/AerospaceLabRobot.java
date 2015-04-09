package team1.robots;

import team1.common.Action;
import team1.common.Robot;
import team1.common.Util;
import team1.constants.UnitConstants;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class AerospaceLabRobot extends Robot {

	public AerospaceLabRobot(RobotController rc) {
		super(rc);
	}

	@Override
	public String name() {
		return "AerospaceLab";
	}
	
	@Override
	public void update() {

	}
	
	@Override
	public void run() throws Exception {
		if (rc.isCoreReady() && rc.getTeamOre() >= UnitConstants.LAUNCHER_ORE_COST) {
			Action.trySpawn(Util.getRandomDirection(),RobotType.LAUNCHER, rc);
		}
	}
}
