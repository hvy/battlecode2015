package team1.robots;

import team1.common.Action;
import team1.common.Robot;
import team1.common.Util;
import team1.constants.BroadcastChannel;
import team1.constants.UnitConstants;
import battlecode.common.Direction;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class TrainFieldRobot extends Robot {

	public TrainFieldRobot(RobotController rc) {
		super(rc);
	}

	@Override
	public void update() {

	}
	
	@Override
	public String name() {
		return "Training Field";
	}
	
	@Override
	public void run() throws Exception {

		if (rc.isCoreReady() && rc.getTeamOre() >= UnitConstants.COMMANDER_ORE_COST) {
			Action.trySpawn(Util.getRandomDirection(),RobotType.COMMANDER, rc);
		}
	}
}