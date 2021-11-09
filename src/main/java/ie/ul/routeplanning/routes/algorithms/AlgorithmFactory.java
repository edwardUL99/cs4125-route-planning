package ie.ul.routeplanning.routes.algorithms;

import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.graph.weights.WeightFunction;

/**
 * A factory for creating algorithms
 */
public class AlgorithmFactory {
    /**
     * Create an algorithm that implements Dijkstra's shortest path algorithm
     * @param start the start waypoint
     * @param end the end waypoint
     * @param weightFunction the weight function used to calculate the weights of the edges
     * @return the algorithm implementing Dijkstra's
     */
    public static Algorithm<Route> dijkstraAlgorithm(Waypoint start, Waypoint end, WeightFunction weightFunction) {
        return new DijkstraAlgorithm(start, end, weightFunction);
    }

    /**
     * Create an algorithm that implements an algorithm that returns the top numRoutes shortest paths
     * @param start the start waypoint
     * @param end the end waypoint
     * @param weightFunction the weight function used to calculate the weights of the edges
     * @param numRoutes the number of routes to generate
     * @return the algorithm implementing Dijkstra's
     */
    public static Algorithm<Route> topKPathsAlgorithm(Waypoint start, Waypoint end, WeightFunction weightFunction, int numRoutes) {
        return new TopKAlgorithm(start, end, weightFunction, numRoutes);
    }
}
