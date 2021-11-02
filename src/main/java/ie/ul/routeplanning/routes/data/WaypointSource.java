package ie.ul.routeplanning.routes.data;

import ie.ul.routeplanning.routes.Waypoint;

import java.util.List;

/**
 * This interface describes an object that can retrieve waypoints from somewhere and read them into the system
 */
public interface WaypointSource {
	/**
	 * Retrieve the waypoints from the waypoint source and return the list of waypoints
	 * @return the list of retrieved way points. The return value shouldn't be null, but can be empty, if no waypoints
	 * are found
	 * @throws WaypointException if an error occurs reading the waypoints
	 */
	List<Waypoint> getWaypoints() throws WaypointException;
}
