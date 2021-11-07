package ie.ul.routeplanning.routes.algorithms;

import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.routes.graph.weights.WeightFunction;

/**
 * This class represents the Dijkstra's shortest path algorithm for finding the shortest path based on the provided weight
 * function
 */
public class DijkstraAlgorithm extends PathFindingAlgorithm {
    /**
     * Constructs Dijkstra's algorithm with the provided start and end waypoints and the weight function for calculating
     * the weight of an edge
     * @param start the start waypoint
     * @param end the end waypoint
     * @param weightFunction the weight function for calculating the edge weights
     */
    public DijkstraAlgorithm(Waypoint start, Waypoint end, WeightFunction weightFunction) {
        super(start, end, weightFunction);
    }

    /**
     * Constructs Dijkstra's algorithm with the default weight function
     * @param start the start waypoint
     * @param end the end waypoint
     */
    public DijkstraAlgorithm(Waypoint start, Waypoint end) {
        super(start, end);
    }

    /**
     * Perform the algorithm on the provided graph
     *
     * @param graph the graph to perform the algorithm on
     * @return the result of the algorithm
     */
    @Override
    public Result<Route> perform(Graph graph) {
        RouteResult routeResult = new RouteResult();
        // TODO implement Dijkstra's here
        return routeResult;
    }
}
