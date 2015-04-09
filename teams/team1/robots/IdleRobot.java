package team1.robots;

import team1.common.Robot;
import battlecode.common.RobotController;

public class IdleRobot extends Robot {

	public IdleRobot(RobotController rc) {
		super(rc);
	}

	@Override
	public void run() throws Exception {
		// Do nothing
	}

	@Override
	public void update() {
		
	}

	@Override
	public String name() {
		return "IdleRobot";
	}
}
