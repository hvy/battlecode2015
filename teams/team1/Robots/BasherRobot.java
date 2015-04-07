package team1.robots;

import team1.common.Action;
import team1.common.Robot;
import team1.common.Util;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

public class BasherRobot extends Robot {
	
	
	public BasherRobot(RobotController rc) {
		super(rc);
	}

	@Override
	public void run() throws Exception {
		RobotInfo[] adjacentEnemies = rc.senseNearbyRobots(2, enemyTeam);

		// BASHERs attack automatically, so let's just move around mostly randomly
		if (rc.isCoreReady()) {
			int fate = rand.nextInt(1000);
			if (fate < 800) {
				Action.tryMove(Util.directions[rand.nextInt(8)], rc);
			} else {
				Action.tryMove(rc.getLocation().directionTo(rc.senseEnemyHQLocation()), rc);
			}
		}
		
	}

	@Override
	public String name() {
		return "Basher";
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
