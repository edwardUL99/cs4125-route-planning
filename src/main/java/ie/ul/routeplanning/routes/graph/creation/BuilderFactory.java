package ie.ul.routeplanning.routes.graph.creation;

import ie.ul.routeplanning.routes.waypoints.WaypointSource;
import ie.ul.routeplanning.transport.TransportMethod;

import java.io.File;
import java.util.Map;

/**
 * This factory provides a means of creating GraphBuilders
 */
public final class BuilderFactory {
    /**
     * Construct a GraphBuilder to read from the provided file name and WaypointSource. The file is to be defined as the
     * edges of the graph and the waypoint source returns all waypoints for these edges
     * @param file the file path providing the graph edges
     * @param waypointSource the waypoint source providing the waypoints for the provided edges file
     * @param transportMethods a map mapping name to transport methods. Expected to have all the supported transport methods in the file
     * @return the GraphBuilder to build the graph from the edges file and provided waypoint source
     */
    public static GraphBuilder fromFile(String file, WaypointSource waypointSource, Map<String, TransportMethod> transportMethods) {
        return new JSONGraphBuilder(file, waypointSource, transportMethods); // current implementation of graph builder from a file is using JSON
    }

    /**
     * Construct a GraphBuilder to read from the provided file and WaypointSource. The file is to be defined as the
     * edges of the graph and the waypoint source returns all waypoints for these edges
     * @param file the file providing the graph edges
     * @param waypointSource the waypoint source providing the waypoints for the provided edges file
     * @param transportMethods a map mapping name to transport methods. Expected to have all the supported transport methods in the file
     * @return the GraphBuilder to build the graph from the edges file and provided waypoint source
     */
    public static GraphBuilder fromFile(File file, WaypointSource waypointSource, Map<String, TransportMethod> transportMethods) {
        return fromFile(file.getAbsolutePath(), waypointSource, transportMethods);
    }
}
