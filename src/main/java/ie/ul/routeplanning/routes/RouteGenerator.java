package ie.ul.routeplanning.routes;

import ie.ul.routeplanning.routes.algorithms.Algorithm;
import ie.ul.routeplanning.routes.algorithms.AlgorithmFactory;
import ie.ul.routeplanning.routes.algorithms.Result;
import ie.ul.routeplanning.routes.algorithms.TopKAlgorithm;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.routes.graph.weights.WeightFunction;
import ie.ul.routeplanning.routes.graph.weights.WeightFunctionBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * This class generates the route based on parameters passed in from the route controller
 */
public final class RouteGenerator {
    /**
     * Generates the routes with the first route being the shortest and then the subsequent routes being the next shortest
     * @param graph the graph used to generate the routes
     * @param start the start waypoint
     * @param end the end waypoint
     * @param ecoFriendly true if emissions should be taken into account, false if not
     * @return the list of generated routes
     */
    public static List<Route> generateRoutes(Graph graph, Waypoint start, Waypoint end, boolean ecoFriendly) {
        graph = graph.copy();
        WeightFunction weightFunction = new WeightFunctionBuilder().withEmissions(ecoFriendly).build();
        Algorithm<Route> algorithm = AlgorithmFactory.dijkstraAlgorithm(start, end, weightFunction);
        Result<Route> result = algorithm.perform(graph);

        List<Route> routes = new ArrayList<>(result.collect());

        if (routes.size() == 1) // remove the first edge so we can find a new route on the next run of the algorithm
            TopKAlgorithm.disconnectRouteEdge(graph, routes.get(0));

        algorithm = AlgorithmFactory.topKPathsAlgorithm(start, end, weightFunction, 3);
        routes.addAll(algorithm.perform(graph).collect());

        return routes;
    }
}
