package ie.ul.routeplanning.services;

import ie.ul.routeplanning.routes.Waypoint;

/**
 * A service for working with waypoints
 */
public interface WaypointService {
    /**
     * Finds the first waypoint with the provided name
     * @param name the name of the waypoint to find
     * @return the first waypoint containing the provided name, null if none found
     */
    Waypoint findWaypoint(String name);
}
