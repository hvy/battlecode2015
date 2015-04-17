package team1;

import battlecode.common.*;

import java.util.*;

import team1.common.Robot;
import team1.robots.AerospaceLabRobot;
import team1.robots.BarracksRobot;
import team1.robots.BasherRobot;
import team1.robots.BeaverRobot;
import team1.robots.DroneRobot;
import team1.robots.HQRobot;
import team1.robots.HelipadRobot;
import team1.robots.IdleRobot;
import team1.robots.LauncherRobot;
import team1.robots.MinerFactoryRobot;
import team1.robots.MinerRobot;
import team1.robots.MissileRobot;
import team1.robots.SoldierRobot;
import team1.robots.TankFactoryRobot;
import team1.robots.TankRobot;
import team1.robots.TowerRobot;

public class RobotPlayer {

	static Robot robot;
	
	public static void run(RobotController rc) {
		Direction lastDirection = null;
		
		setRobot(rc);

		while(true) {
			try {
				//rc.setIndicatorString(0, "This is an indicator string.");
				//rc.setIndicatorString(1, "I am a " + rc.getType());
			} catch (Exception e) {
				//System.out.println("Unexpected exception");
				e.printStackTrace();
			}

			try {
				robot.update(); // Update robot data TODO Remove update() and do it inside run()
				robot.run(); // Execute AI
			} catch (Exception e) {
				System.out.println(robot.name() + " Exception");
				e.printStackTrace();
			}
			
			rc.yield();
		}
	}
	
	private static void setRobot(RobotController rc) {
		
		switch (rc.getType()) {
		case HQ:
			robot = new HQRobot(rc);
			break;
		case TOWER:
			robot = new TowerRobot(rc);
			break;
		case BEAVER:
			robot = new BeaverRobot(rc);
			break;
		case MINER:
			robot = new MinerRobot(rc);
			break;
		case SOLDIER:
			robot = new SoldierRobot(rc);
			break;
		case BASHER:
			robot = new BasherRobot(rc);
			break;
		case TANK:
			robot = new TankRobot(rc);
			break;
		case DRONE:
			robot = new DroneRobot(rc);
			break;
		case LAUNCHER:
			robot = new LauncherRobot(rc);
			break;
		case MISSILE:
			robot = new MissileRobot(rc);
			break;
		case MINERFACTORY:
			robot = new MinerFactoryRobot(rc);
			break;
		case BARRACKS:
			robot = new BarracksRobot(rc);
			break;
		case TANKFACTORY:
			robot = new TankFactoryRobot(rc);
			break;
		case HELIPAD:
			robot = new HelipadRobot(rc);
			break;
		case AEROSPACELAB:
			robot = new AerospaceLabRobot(rc);
			break;
		case SUPPLYDEPOT:
		case COMPUTER:
		case HANDWASHSTATION:
			robot = new IdleRobot(rc);
			break;
		default:
			System.out.println("Unknown/Unimplemented robot cannot be instantiated.");
			break;
		}
	}	
}
