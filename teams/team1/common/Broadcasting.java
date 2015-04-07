package team1.common;

import team1.constants.BroadcastChannel;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Broadcasting {
	
	private RobotController rc;
	
	public Broadcasting(RobotController rc) {
		this.rc = rc;
	}
	
	public void sendInt(int type, int message) throws GameActionException {
		rc.broadcast(type, message);
	}
	
	public int readInt(int type) throws GameActionException {
		return rc.readBroadcast(type);
	}
	
	public void sendLocation(int type, MapLocation loc) throws GameActionException {
		rc.broadcast(type, loc.x);
		rc.broadcast(type+1, loc.y);
	}
	
	public MapLocation readLocation(int type) throws GameActionException {
		int x = rc.readBroadcast(type);
		int y = rc.readBroadcast(type+1);
		return new MapLocation(x,y);
	}
	
	public boolean setPreferredStructure(RobotType structureType) {
		try {
			rc.broadcast(BroadcastChannel.PREFERRED_STRUCTURE, structureType.ordinal());
		} catch (GameActionException e) {
			return false;
		}
		return true;
	}
	
	public RobotType readPreferredStructure() {
		RobotType structureType = null;
		try {
			int robotTypeOrdinal = rc.readBroadcast(BroadcastChannel.PREFERRED_STRUCTURE);
			structureType = RobotType.values()[robotTypeOrdinal];
		} catch (GameActionException e) {
			return null;
		}
		return structureType;
	}
	
	 public int channelFromLocation(MapLocation loc) {
	 	int x = loc.x;
	 	int y = loc.y;
        x %= GameConstants.MAP_MAX_WIDTH;
        if (x < 0) x += GameConstants.MAP_MAX_WIDTH;
        y %= GameConstants.MAP_MAX_HEIGHT;
        if (y < 0) y += GameConstants.MAP_MAX_HEIGHT;
        return BroadcastChannel.MISSILE_BASE + y * GameConstants.MAP_MAX_WIDTH + x;
    }
	 
}
