package team1.robots;

import java.util.Random;

import team1.common.Action;
import team1.common.Parameters;
import team1.common.Robot;
import team1.common.SupplyHandler;
import team1.common.Util;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;

public class CommanderRobot extends Robot {
	
	MapLocation harassLocation;

	
	public CommanderRobot(RobotController rc) {
		super(rc);
	}

	@Override
	public void run() throws Exception {
		if (rc.isWeaponReady()) {
			Action.attackSomething(myRange, enemyTeam, rc);
//			return;
		}
		
//		if (rc.getXP() > 2000) {
////			harassMiners();
//			Action.tryMove(rc.getLocation().directionTo(rc.senseEnemyTowerLocations()[0]), rc);
//			return;
//		}
			
		
		SupplyHandler.shareSupply(this);
	    SupplyHandler.requestResupplyIfNecessary(this);
	    
	    
		if (rc.isCoreReady() && !tryToRetreat(rc.senseNearbyRobots(20, enemyTeam))) {
			int fate = rand.nextInt(1000);
			if (fate < 1) {
				Action.tryMove(Util.directions[rand.nextInt(8)], rc);
			} else {
				
				//Action.tryMove(rc.getLocation().directionTo(rc.senseEnemyHQLocation()), rc);
				if (rc.senseTowerLocations().length == 0)
					Action.tryMove(rc.getLocation().directionTo(home), rc);
				else 
					Action.tryMove(rc.getLocation().directionTo(getCheckpoint2()), rc);
			}
		}
		
	}
	
	private void harassMiners() throws GameActionException {
		RobotInfo[] enemies = rc.senseNearbyRobots(150, enemyTeam);
		
		for (int i = 0; i < enemies.length; i++) {
			if (enemies[i].type == RobotType.MINER) {
				harassLocation = enemies[i].location;
				break;
			}
		}
		
	}
	
	   // commander flash range:
    // . . . . . . . . .
    // . . . f f f . . .
    // . . f f f f f . .
    // . f f f f f f f .
    // . f f f C f f f .
    // . f f f f f f f .
    // . . f f f f f . .
    // . . . f f f . . .
    // . . . . . . . . .
    static int[] flashMaxRangeDxs = new int[] { 0, 1, 2, 3, 3, 3, 2, 1, 0, -1, -2, -3, -3, -3, -2, -1 };
    static int[] flashMaxRangeDys = new int[] { 3, 3, 2, 1, 0, -1, -2, -3, -3, -3, -2, -1, 0, 1, 2, 3 };
    static final int numFlashMaxRangeSquares = 16;

    private boolean tryToRetreat(RobotInfo[] nearbyEnemies) throws GameActionException {
    	if (nearbyEnemies.length <= 0)
    		return false;
    	MapLocation[] enemyTowers = rc.senseEnemyTowerLocations();
    	
        Direction bestRetreatDir = null;
        RobotInfo currentClosestEnemy = Util.closest(nearbyEnemies, rc.getLocation());

        int bestDistSq = rc.getLocation().distanceSquaredTo(currentClosestEnemy.location);
        for (Direction dir : Direction.values()) {
            if (!rc.canMove(dir)) continue;

            MapLocation retreatLoc = rc.getLocation().add(dir);
            if (inEnemyTowerOrHQRange(retreatLoc, enemyTowers)) continue;

            RobotInfo closestEnemy = Util.closest(nearbyEnemies, retreatLoc);
            int distSq = retreatLoc.distanceSquaredTo(closestEnemy.location);
            if (distSq > bestDistSq) {
                bestDistSq = distSq;
                bestRetreatDir = dir;
            }
        }

        if (rc.getFlashCooldown() == 0) {
            MapLocation bestFlashLoc = null;
            for (int i = 0; i < numFlashMaxRangeSquares; i++) {
                MapLocation flashLoc = rc.getLocation().add(flashMaxRangeDxs[i], flashMaxRangeDys[i]);
                if (!rc.isPathable(RobotType.COMMANDER, flashLoc)) continue;
                if (inEnemyTowerOrHQRange(flashLoc, enemyTowers)) continue;
                RobotInfo closestEnemy = Util.closest(nearbyEnemies, flashLoc);
                int distSq = flashLoc.distanceSquaredTo(closestEnemy.location);
                if (distSq > bestDistSq) {
                    bestDistSq = distSq;
                    bestFlashLoc = flashLoc;
                }
            }
            if (bestFlashLoc != null) {
                rc.castFlash(bestFlashLoc);
                return true;
            }
        }

        if (bestRetreatDir != null) {
            rc.move(bestRetreatDir);
            return true;
        }
        return false;
    }
	

	@Override
	public String name() {
		return "Commander";
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
