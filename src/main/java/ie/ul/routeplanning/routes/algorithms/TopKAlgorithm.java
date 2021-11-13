package ie.ul.routeplanning.routes.algorithms;

import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.routes.graph.weights.WeightFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates the top k algorithm for finding the top k shortest routes
 */
public class TopKAlgorithm extends DijkstraAlgorithm {
    /**
     * The number of routes to generate
     */
    private final int numRoutes;

    /**
     * Constructs the algorithm with the provided start and end waypoints and the weight function for calculating
     * the weight of an edge
     *
     * @param start          the start waypoint
     * @param end            the end waypoint
     * @param weightFunction the weight function for calculating the edge weights
     * @param numRoutes      the number of routes (at most) that should be generated
     */
    public TopKAlgorithm(Waypoint start, Waypoint end, WeightFunction weightFunction, int numRoutes) {
        super(start, end, weightFunction);
        this.numRoutes = numRoutes;
    }

    /**
     * Constructs the algorithm with the default weight function
     *
     * @param start the start waypoint
     * @param end   the end waypoint
     * @param numRoutes the number of routes (at most) that should be generated
     */
    public TopKAlgorithm(Waypoint start, Waypoint end, int numRoutes) {
        super(start, end);
        this.numRoutes = numRoutes;
    }

    /**
     * Perform the algorithm on the provided graph
     *
     * @param graph the graph to perform the algorithm on
     * @return the result of the algorithm
     */
    @Override
    public Result<Route> perform(Graph graph) {
       List<Route> generatedRoutes = new ArrayList<>();

       while (generatedRoutes.size() < numRoutes) {
           Route route = dijkstra(graph);

           if (route == null)
               break;

           disconnectRouteEdge(graph, route);
           generatedRoutes.add(route);
       }

       return new RouteResult(generatedRoutes);
    }

    /**
     * Removes the first edge of the provided route from the graph, essentially disconnected the connection that allowed
     * a route to be generated. This is so that dijkstra's can try and find a new route the other time.
     *
     * The graph should be copied before passing it into this algorithm
     * @param graph the graph to remove the edge from
     * @param route the route to remove the edge from
     */
    public static void disconnectRouteEdge(Graph graph, Route route) {
        graph.removeEdge(route.getRouteLegs().get(0));
    }
}
