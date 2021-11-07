package ie.ul.routeplanning.routes.algorithms;

import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.routes.graph.weights.WeightFunction;
import ie.ul.routeplanning.routes.graph.weights.WeightFunctionBuilder;

import java.util.List;

/**
 * An abstract class that is the base of all path finding algorithms.
 *
 * TODO provide some default operations, can be an example of template method too
 */
public abstract class PathFindingAlgorithm implements Algorithm<Route> {
    /**
     * The start waypoint for the path finding
     */
    private final Waypoint start;
    /**
     * The end waypoint for the path finding
     */
    private final Waypoint end;
    /**
     * The weight function to use for calculating weights
     */
    private final WeightFunction weightFunction;

    /**
     * Construct an algorithm for path finding with the provided parameters
     * @param start the start waypoint for the path finding
     * @param end the end waypoint for the path finding
     * @param weightFunction the weight function used to calculate weights
     */
    public PathFindingAlgorithm(Waypoint start, Waypoint end, WeightFunction weightFunction) {
        this.start = start;
        this.end = end;
        this.weightFunction = weightFunction;
    }

    /**
     * Construct an algorithm for path finding using a default WeightFunction
     * @param start the start waypoint for the path finding
     * @param end the end waypoint for the path finding
     */
    public PathFindingAlgorithm(Waypoint start, Waypoint end) {
        this(start, end, WeightFunctionBuilder.DEFAULT);
    }

    /**
     * A delegate function to the algorithm method
     * @param graph the graph to perform the algorithm on
     * @return the list of routes
     */
    public List<Route> generateRoutes(Graph graph) {
        return perform(graph).collect();
    }

    /**
     * Get the start waypoint used for generating the path
     * @return the start waypoint
     */
    public Waypoint getStart() {
        return start;
    }

    /**
     * Get the end waypoint used for generating the path
     * @return the end waypoint
     */
    public Waypoint getEnd() {
        return end;
    }

    /**
     * Get the weight function used for calculating edge weights
     * @return the weight function used to calculate edge weights
     */
    public WeightFunction getWeightFunction() {
        return weightFunction;
    }

    /**
     * Perform the algorithm on the provided graph
     *
     * @param graph the graph to perform the algorithm on
     * @return the result of the algorithm
     */
    @Override
    public abstract Result<Route> perform(Graph graph);
}
