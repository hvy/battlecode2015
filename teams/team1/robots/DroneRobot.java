package team1.robots;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import team1.common.Action;
import team1.common.Robot;
import team1.common.SupplyHandler;

public class DroneRobot extends Robot {

	public DroneRobot(RobotController rc) {
		super(rc);
	}

	@Override
	public void run() throws Exception {
		if (rc.isCoreReady())
			SupplyHandler.runSupplies(this);
//			runSupplies();
	}
	
	private void runSupplies() throws GameActionException {
		if (rc.getSupplyLevel() <= 0)
			Action.tryMove(rc.getLocation().directionTo(home), rc);
		else
			Action.tryMove(rc.getLocation().directionTo(getCheckpoint()), rc);
		
	}

	@Override
	public void update() {

	}

	@Override
	public String name() {
		return "Drone";
	}

}
