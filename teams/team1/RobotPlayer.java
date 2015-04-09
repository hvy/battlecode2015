package team1;

import battlecode.common.*;

import java.util.*;

import team1.common.Robot;
import team1.robots.AerospaceLabRobot;
import team1.robots.BarracksRobot;
import team1.robots.BasherRobot;
import team1.robots.BeaverRobot;
import team1.robots.FactoryRobot;
import team1.robots.HQRobot;
import team1.robots.HelipadRobot;
import team1.robots.IdleRobot;
import team1.robots.MinerFactoryRobot;
import team1.robots.MinerRobot;
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
		case BASHER:
			robot = new BasherRobot(rc);
		case TANK:
			robot = new TankRobot(rc);
		case MINERFACTORY:
			robot = new MinerFactoryRobot(rc);
			break;
		case TECHNOLOGYINSTITUTE:
		case TRAININGFIELD:
		case BARRACKS:
		case TANKFACTORY:
		case HELIPAD:
		case AEROSPACELAB:
			robot = new FactoryRobot(rc);
			break;
		case SUPPLYDEPOT:
		case COMPUTER:
		case HANDWASHSTATION:
			robot = new IdleRobot(rc);
			break;
		default:
			break;
		}
	}	
}
