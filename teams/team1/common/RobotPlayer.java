package team1.common;

import battlecode.common.*;

import java.util.*;

import team1.robots.BarracksRobot;
import team1.robots.BasherRobot;
import team1.robots.BeaverRobot;
import team1.robots.HQRobot;
import team1.robots.HelipadRobot;
import team1.robots.MinerFactoryRobot;
import team1.robots.MinerRobot;
import team1.robots.SoldierRobot;
import team1.robots.SupplyDepotRobot;
import team1.robots.TankFactoryRobot;
import team1.robots.TankRobot;
import team1.robots.TowerRobot;

public class RobotPlayer {

	static Robot robot;
	
	public static void run(RobotController rc) {
		//rand = new Random(rc.getID());
		Direction lastDirection = null;
		
		setRobot(rc);

		while(true) {
			try {
				rc.setIndicatorString(0, "This is an indicator string.");
				rc.setIndicatorString(1, "I am a " + rc.getType());
			} catch (Exception e) {
				System.out.println("Unexpected exception");
				e.printStackTrace();
			}

			try {
				robot.update(); // Update robot data TODO Remove update() and do it inside run()
				robot.run(); // Execute AI
			} catch (Exception e) { 
				System.out.println( robot.name() + " Exception");
				e.printStackTrace();
			}
			
			rc.yield();
		}
	}
	
	private static void setRobot(RobotController rc) {
		
		if (rc.getType() == RobotType.HQ) {
			robot = new HQRobot(rc);
		}

		if (rc.getType() == RobotType.TOWER) {
			robot = new TowerRobot(rc);
		}

		if (rc.getType() == RobotType.BASHER) {
			robot = new BasherRobot(rc);
		}

		if (rc.getType() == RobotType.SOLDIER) {
			robot = new SoldierRobot(rc);
		}

		if (rc.getType() == RobotType.BEAVER) {
			robot = new BeaverRobot(rc);
		}

		if (rc.getType() == RobotType.BARRACKS) {
			robot = new BarracksRobot(rc);
		}
		
		if (rc.getType() == RobotType.HELIPAD) {
			robot = new HelipadRobot(rc);
		}
		
		if (rc.getType() == RobotType.MINERFACTORY) {
			robot = new MinerFactoryRobot(rc);
		}
		
		if (rc.getType() == RobotType.MINER) {
			robot = new MinerRobot(rc);
		}
		
		if (rc.getType() == RobotType.SUPPLYDEPOT) {
			robot = new SupplyDepotRobot(rc);
		}
		
		if (rc.getType() == RobotType.TANK) {
			robot = new TankRobot(rc);
		}
		
		if (rc.getType() == RobotType.TANKFACTORY) {
			robot = new TankFactoryRobot(rc);
		}
	}	
}
