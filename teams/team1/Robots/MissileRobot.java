package team1.robots;

import team1.common.Action;
import team1.common.Robot;
import battlecode.common.*;

public class MissileRobot extends Robot {
	
	private MissileInfo info;
	
	private class MissileInfo {
		public int targetID;
		public MapLocation target;
		
		public MissileInfo(int id, MapLocation tar) {
			targetID = id;
			target = tar;
		}
	}
	
    public MissileRobot(RobotController rc) {
		super(rc);
		// Get missile target information from Launcher
		try {
			info = readMissileTarget(rc, rc.getLocation());
		} catch (GameActionException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() throws Exception {
		RobotInfo[] adjacentEnemies = rc.senseNearbyRobots(2, rc.getTeam().opponent());
        for (int i = adjacentEnemies.length; i-- > 0;) {
            if (adjacentEnemies[i].type != RobotType.MISSILE) rc.explode();
        }

        if (rc.canSenseRobot(info.targetID)) {
        	info.target = rc.senseRobot(info.targetID).location;
        }

        MapLocation here = rc.getLocation();
        Direction dir = here.directionTo(info.target);

        RobotInfo blockage = rc.senseRobotAtLocation(here.add(dir));
        if (blockage != null && !blockage.team.equals(rc.getTeam()) && blockage.type != RobotType.MISSILE) {
            // System.out.println("exploding at blockage");
            rc.explode();
            return;
        }

        if (!rc.isCoreReady())
        	return;

        if (rc.canMove(dir)) {
        	Action.tryMove(dir, rc);
            return;
        }

        Direction left = dir.rotateLeft();
        if (rc.canMove(left)) {
        	Action.tryMove(left, rc);
            return;
        }

        Direction right = dir.rotateRight();
        if (rc.canMove(right)) {
        	Action.tryMove(right, rc);
            return;
        }
		
	}
	
	private MissileInfo readMissileTarget(RobotController rc, MapLocation start) throws GameActionException {
        int data = rc.readBroadcast(broadcast.channelFromLocation(start));

        int dy = (data & 0x0f) - 8;
        int dx = ((data & 0xf0) >> 4) - 8;
        MapLocation receivedTargetLocation = new MapLocation(start.x + dx, start.y + dy);
        int receivedTargetID = (data & 0xfffff00) >> 8;
        
        return new MissileInfo(receivedTargetID, receivedTargetLocation);
    }

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String name() {
		return "Missile";
	}
}