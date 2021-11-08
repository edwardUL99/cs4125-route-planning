package ie.ul.routeplanning.routes.graph.creation;

import ie.ul.routeplanning.routes.data.WaypointSource;

import java.io.File;

/**
 * This factory provides a means of creating GraphBuilders
 */
public final class BuilderFactory {
    /**
     * Construct a GraphBuilder to read from the provided file name and WaypointSource. The file is to be defined as the
     * edges of the graph and the waypoint source returns all waypoints for these edges
     * @param file the file path providing the graph edges
     * @param waypointSource the waypoint source providing the waypoints for the provided edges file
     * @return the GraphBuilder to build the graph from the edges file and provided waypoint source
     */
    public static GraphBuilder fromFile(String file, WaypointSource waypointSource) {
        return new JSONGraphBuilder(file, waypointSource); // current implementation of graph builder from a file is using JSON
    }

    /**
     * Construct a GraphBuilder to read from the provided file and WaypointSource. The file is to be defined as the
     * edges of the graph and the waypoint source returns all waypoints for these edges
     * @param file the file providing the graph edges
     * @param waypointSource the waypoint source providing the waypoints for the provided edges file
     * @return the GraphBuilder to build the graph from the edges file and provided waypoint source
     */
    public static GraphBuilder fromFile(File file, WaypointSource waypointSource) {
        return fromFile(file.getAbsolutePath(), waypointSource);
    }
}
