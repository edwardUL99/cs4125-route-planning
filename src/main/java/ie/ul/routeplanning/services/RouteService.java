package ie.ul.routeplanning.services;

import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.SavedRoute;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.users.User;

import java.util.List;

/**
 * This interface represents a route service for generating routes
 */
public interface RouteService {
    /**
     * Retrieve the route with the provided ID
     * @param id the id of the route to retrieve
     * @return the found route, or null if not found
     */
    Route getRoute(Long id);

    /**
     * Generate the routes using the provided graph and waypoints. It is expected that the first route in the list, if
     * any, is the best route, with the subsequent routes being the next best ones
     * @param graph the graph to generate the route with
     * @param start the starting waypoint
     * @param end the end waypoint
     * @param ecoFriendly true if the routes should be CO2 aware
     * @param time        true if time should be factored into the route duration
     * @return the list of generated routes
     */
    List<Route> generateRoutes(Graph graph, Waypoint start, Waypoint end, boolean ecoFriendly, boolean time);

    /**
     * Save the route with the given user as a SavedRoute
     * @param user the user to save the route on
     * @param route the route to save
     */
    void saveRoute(User user, Route route);

    /**
     * Delete the route with the given id
     * @param id the id of the route to delete
     */
    void deleteRoute(Long id);

    /**
     * Retrieves all the saved routes for the provided user
     * @param user the user to retrieve saved routes for
     * @return the list of saved routes for the provided user
     */
    List<SavedRoute> getSavedRoutes(User user);

    /**
     * This method determines if the provided route is saved by the given user
     * @param user the user to check if the route is saved
     * @param route the route to check if it's saved
     * @return true if saved, false if not
     */
    boolean isRouteSaved(User user, Route route);
}
