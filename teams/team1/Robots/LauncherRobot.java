package team1.robots;

import team1.common.Action;
import team1.common.Robot;
import team1.common.SupplyHandler;
import team1.common.Util;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class LauncherRobot extends Robot {
	
	public LauncherRobot(RobotController rc) {
		super(rc);
	}

	@Override
	public void run() throws Exception {
		
		
		if (rc.getMissileCount() > 0) {
            tryMissileLaunch();
        }
		
		SupplyHandler.shareSupply(this);
	    SupplyHandler.requestResupplyIfNecessary(this);
	     
		if (rc.isCoreReady()) {
			int fate = rand.nextInt(1000);
			if (fate < 30) {
				Action.tryMove(Util.directions[rand.nextInt(8)], rc);
			} else {
				
				if (rc.senseTowerLocations().length == 0)
					Action.tryMove(rc.getLocation().directionTo(home), rc);
				else 
					Action.tryMove(rc.getLocation().directionTo(getCheckpoint()), rc);
			}
		}
		
	}
	
	private boolean tryMissileLaunch() throws GameActionException {
        RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(63, enemyTeam);

        for (RobotInfo enemy : nearbyEnemies) {
            if (enemy.type != RobotType.MISSILE) {
                if (tryLaunchAt(enemy.location)) return true;
            }
        }

        for (MapLocation enemyTower : rc.senseEnemyTowerLocations()) {
            if (location.distanceSquaredTo(enemyTower) <= 49) {
                if (tryLaunchAt(enemyTower)) return true;
            }
        }

        if (location.distanceSquaredTo(enemyHome) <= 49) {
            if (tryLaunchAt(enemyHome)) return true;
        }

        return false;
    }

    private boolean tryLaunchAt(MapLocation enemyLoc) throws GameActionException {
        if (location.distanceSquaredTo(enemyLoc) <= 8) {
            // can't shoot directly at nearby enemies without hurting ourselves unless we are clever
            // hopefuly this is half-clever: if we want to shoot at an adjacent enemy, wait until we can kite back in the same turn
            // this is not a huge burden because our move delay is 4 while our missile spawn delay is 8
            if (rc.getCoreDelay() >= 1) return false;

            // adjacent enemies are a special case, since we have to fire to the side
            if (location.isAdjacentTo(enemyLoc)) {
                Direction toEnemy = location.directionTo(enemyLoc);
                MapLocation target1 = location.add(toEnemy.rotateLeft());
                if (rc.isPathable(RobotType.MISSILE, target1)) {
                    rc.launchMissile(toEnemy.rotateLeft());
                    setMissileTarget(rc, target1, target1);
                    return true;
                }
                MapLocation target2 = location.add(toEnemy.rotateRight());
                if (rc.isPathable(RobotType.MISSILE, target2)) {
                    rc.launchMissile(toEnemy.rotateRight());
                    setMissileTarget(rc, target2, target2);
                    return true;
                }
                return false;
            }
        }

        boolean clearPath = true;
        MapLocation loc = location;
        while (true) {
            loc = loc.add(loc.directionTo(enemyLoc));
            RobotInfo robotInWay = rc.canSenseLocation(loc) ? rc.senseRobotAtLocation(loc) : null;
            if (robotInWay != null && robotInWay.type != RobotType.MISSILE) {
                clearPath = false;
                break;
            }
            if (loc.isAdjacentTo(enemyLoc)) {
                RobotInfo[] alliesInFriendlyFire = rc.senseNearbyRobots(loc, 2, myTeam);
                for (RobotInfo ally : alliesInFriendlyFire) {
                    if (ally.type != RobotType.MISSILE) {
                        clearPath = false;
                        break;
                    }
                }
                break;
            }
        }

        if (clearPath) {
            Direction dir = location.directionTo(enemyLoc);
            if (rc.canLaunch(dir)) {
                rc.launchMissile(dir);
                setMissileTarget(rc, location.add(dir), enemyLoc);
                return true;
            }
        }
        return false;
    }
    
    private void setMissileTarget(RobotController rc, MapLocation start, MapLocation targetLocation) throws GameActionException {
        int dx = targetLocation.x - start.x;
        int dy = targetLocation.y - start.y;
        
        int targetID = 0;
        if(rc.canSenseLocation(targetLocation)) {
            RobotInfo targetRobot = rc.senseRobotAtLocation(targetLocation);
            if(targetRobot != null) targetID = targetRobot.ID;
        }
        
        int data = (targetID << 8) + ((8 + dx) << 4) + (8 + dy);
        broadcast.sendInt(broadcast.channelFromLocation(start), data);
    }


	@Override
	public String name() {
		return "Launcher";
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
