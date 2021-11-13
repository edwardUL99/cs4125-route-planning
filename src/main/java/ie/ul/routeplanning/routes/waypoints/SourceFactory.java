package ie.ul.routeplanning.routes.waypoints;

import ie.ul.routeplanning.routes.Waypoint;

import java.io.File;
import java.util.List;

/**
 * This factory provides methods to create WaypointSource objects
 */
public final class SourceFactory {
    /**
     * Construct a WaypointSource to read from the provided file name. The file is to be defined as the
     * waypoints of the graph in JSON format and the waypoint source returns all waypoints for these edges
     * @param file the file path providing the waypoint definitions
     * @return the WaypointSource to read waypoints from the file
     */
    public static WaypointSource fromFile(String file) {
        return new JSONWaypointSource(file); // the default source for waypoints from a file is through JSON
    }

    /**
     * Construct a WaypointSource to read from the provided file. The file is to be defined as the
     * waypoints of the graph in JSON format and the waypoint source returns all waypoints for these edges
     * @param file the file providing the waypoint definitions
     * @return the WaypointSource to read waypoints from the file
     */
    public static WaypointSource fromFile(File file) {
        return fromFile(file.getAbsolutePath());
    }

    /**
     * Construct a WaypointSource from the provided waypoints
     * @param waypoints the waypoints to create a source from
     * @return the waypoint source to read from the list
     */
    public static WaypointSource fromList(List<Waypoint> waypoints) {
        return new ListWaypointSource(waypoints);
    }
}
