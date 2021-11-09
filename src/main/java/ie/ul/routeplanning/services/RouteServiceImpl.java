package ie.ul.routeplanning.services;

import ie.ul.routeplanning.constants.Constant;
import ie.ul.routeplanning.repositories.WaypointRepository;
import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.algorithms.Algorithm;
import ie.ul.routeplanning.routes.algorithms.AlgorithmFactory;
import ie.ul.routeplanning.routes.algorithms.Result;
import ie.ul.routeplanning.routes.algorithms.TopKAlgorithm;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.routes.graph.weights.WeightFunction;
import ie.ul.routeplanning.routes.graph.weights.WeightFunctionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The implementation for our route service
 */
@Service
public class RouteServiceImpl implements RouteService {
    /**
     * The waypoint repository used for finding waypoints
     */
    @Autowired
    private WaypointRepository waypointRepository;

    /**
     * Generate the routes using the provided graph and waypoints. It is expected that the first route in the list, if
     * any, is the best route, with the subsequent routes being the next best ones
     *
     * @param graph       the graph to generate the route with
     * @param start       the starting waypoint
     * @param end         the end waypoint
     * @param ecoFriendly true if the routes should be CO2 aware
     * @return the list of generated routes
     */
    @Override
    public List<Route> generateRoutes(Graph graph, Waypoint start, Waypoint end, boolean ecoFriendly) {
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

    /**
     * Find a waypoint with the provided name
     * @param name the name of the waypoint
     * @return an optional containing the waypoint, empty if not found
     */
    private Optional<Waypoint> findWaypoint(String name) {
        return waypointRepository.findByName(name).stream().findFirst();
    }

    /**
     * Validates and parses the parameters for route generation. If not valid, an "error" flash attribute is set in the model and a
     * validated parameters object is returned with invalid as true
     *
     * @param startWaypoint the name of the start waypoint
     * @param endWaypoint   the name of the end waypoint
     * @return an instance of ValidatedParameters with the new parameters and found waypoints
     */
    @Override
    public RouteParameters parseParameters(String startWaypoint, String endWaypoint) {
        startWaypoint = Constant.capitalise(startWaypoint);
        endWaypoint = Constant.capitalise(endWaypoint);

        Optional<Waypoint> startOpt = findWaypoint(startWaypoint);
        Optional<Waypoint> endOpt = findWaypoint(endWaypoint);
        Waypoint start = null, end = null;

        String error;

        if (startWaypoint.equals(endWaypoint)) {
            error = "You cannot create a route with the same start and end waypoints";
        } else if (!startOpt.isPresent()) {
            error = String.format("No start waypoint found with name %s", startWaypoint);
        } else if (!endOpt.isPresent()) {
            error = String.format("No end waypoint found with name %s", endWaypoint);
        } else {
            start = startOpt.get();
            end = endOpt.get();
            error = null;
        }

        if (error != null) {
            return RouteParameters.createInvalid(error);
        } else {
            return RouteParameters.createValid(start, end);
        }
    }
}
