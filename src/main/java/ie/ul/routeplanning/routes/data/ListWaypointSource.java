package ie.ul.routeplanning.routes.data;

import ie.ul.routeplanning.routes.Waypoint;

import java.util.ArrayList;
import java.util.List;

/**
 * A waypoint source that just reads from a list
 */
public class ListWaypointSource implements WaypointSource {
    /**
     * The waypoints returned by the waypoint source
     */
    private final List<Waypoint> waypoints;

    /**
     * Construct a ListWaypointSource with the provided waypoints
     * @param waypoints the waypoints to provide
     */
    public ListWaypointSource(List<Waypoint> waypoints) {
        this.waypoints = new ArrayList<>(waypoints);
    }

    /**
     * Retrieve the waypoints from the waypoint source and return the list of waypoints
     *
     * @return the list of retrieved way points. The return value shouldn't be null, but can be empty, if no waypoints
     * are found
     */
    @Override
    public List<Waypoint> getWaypoints() {
        return waypoints;
    }
}
