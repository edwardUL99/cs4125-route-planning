package ie.ul.routeplanning.services;

import ie.ul.routeplanning.repositories.RouteRepository;
import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.SavedRoute;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.algorithms.*;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.routes.graph.weights.WeightFunction;
import ie.ul.routeplanning.routes.graph.weights.WeightFunctionBuilder;
import ie.ul.routeplanning.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation for our route service
 */
@Service
public class RouteServiceImpl implements RouteService {
    /**
     * The RouteRepository for saving/loading routes
     */
    private final RouteRepository routeRepository;
    /**
     * The context for working with algorithms
     */
    private final AlgorithmContext<Route> algorithmContext = new AlgorithmContext<>();

    /**
     * Creates a RouteServiceImpl with the provided route repository dependency
     * @param routeRepository the route repository for saving/loading routes
     */
    @Autowired
    public RouteServiceImpl(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    /**
     * Retrieve the route with the provided ID
     *
     * @param id the id of the route to retrieve
     * @return the found route, or null if not found
     */
    @Override
    public Route getRoute(Long id) {
        return routeRepository.findById(id).orElse(null);
    }

    /**
     * Sets up the algorithm context for this algorithm execution and returns the result
     * @param graph       the graph to generate the route with
     * @param start       the starting waypoint
     * @param end         the end waypoint
     * @param ecoFriendly true if the routes should be CO2 aware
     * @param time        true if time should be factored into the route duration
     * @return the list of generated routes
     */
    private List<Route> performAlgorithm(Graph graph, Waypoint start, Waypoint end, boolean ecoFriendly, boolean time) {
        WeightFunction weightFunction = new WeightFunctionBuilder().withEmissions(ecoFriendly).withTime(time).build();
        // since topKPaths is an extension of dijkstras, we can ask for 4 routes and the 1st route will be the best
        Algorithm<Route> algorithm = AlgorithmFactory.topKPathsAlgorithm(start, end, weightFunction, 4);

        algorithmContext.setAlgorithm(algorithm);
        algorithmContext.setGraph(graph);
        Result<Route> result = algorithmContext.perform();

        algorithmContext.reset();

        return new ArrayList<>(result.collect());
    }

    /**
     * Generate the routes using the provided graph and waypoints. It is expected that the first route in the list, if
     * any, is the best route, with the subsequent routes being the next best ones
     *
     * @param graph       the graph to generate the route with
     * @param start       the starting waypoint
     * @param end         the end waypoint
     * @param ecoFriendly true if the routes should be CO2 aware
     * @param time        true if time should be factored into the route duration
     * @return the list of generated routes
     */
    @Override
    public List<Route> generateRoutes(Graph graph, Waypoint start, Waypoint end, boolean ecoFriendly, boolean time) {
        List<Route> routes = performAlgorithm(graph, start, end, ecoFriendly, time);
        routeRepository.saveAll(routes);

        return routes;
    }

    /**
     * Save the route with the given user as a SavedRoute
     *
     * @param user  the user to save the route on
     * @param route the route to save
     */
    @Override
    public void saveRoute(User user, Route route) {
        SavedRoute savedRoute = new SavedRoute(null, user, route);
        routeRepository.save(savedRoute);
    }

    /**
     * Delete the route with the given id
     *
     * @param id the id of the route to delete
     */
    @Override
    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }

    /**
     * Retrieves all the saved routes for the provided user
     *
     * @param user the user to retrieve saved routes for
     * @return the list of saved routes for the provided user
     */
    @Override
    public List<SavedRoute> getSavedRoutes(User user) {
        return routeRepository.findSavedRoutesByUsername(user.getUsername());
    }

    /**
     * This method determines if the provided route is saved by the given user
     *
     * @param user  the user to check if the route is saved
     * @param route the route to check if it's saved
     * @return true if saved, false if not
     */
    @Override
    public boolean isRouteSaved(User user, Route route) {
        return getSavedRoutes(user)
                .stream()
                .anyMatch(s -> s.getSavedRoute().getId().equals(route.getId()));
    }
}
