package ie.ul.routeplanning.services;

import ie.ul.routeplanning.routes.Route;
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
     * @return the list of generated routes
     */
    List<Route> generateRoutes(Graph graph, Waypoint start, Waypoint end, boolean ecoFriendly);

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
    List<Route> getSavedRoutes(User user);

    /**
     * Validates and parses the parameters for route generation. If not valid, an "error" flash attribute is set in the model and a
     * validated parameters object is returned with invalid as true
     * @param startWaypoint the name of the start waypoint
     * @param endWaypoint the name of the end waypoint
     * @return an instance of ValidatedParameters with the new parameters and found waypoints
     */
    RouteParameters parseParameters(String startWaypoint, String endWaypoint);

    /**
     * A class to return as a result of validating the parameters.
     * Only instantiable within the RouteService
     */
    class RouteParameters {
        /**
         * Indicates if this represents a valid parameter set
         */
        private final boolean valid;
        /**
         * The error message
         */
        private final String error;
        /**
         * The start waypoint that has been retrieved from the parameters
         */
        private final Waypoint start;
        /**
         * The end waypoint that has been retrieved from the parameters
         */
        private final Waypoint end;

        /**
         * Create a RouteParameters instance
         * @param valid true if valid, false if not
         * @param error if valid is false, this contains the error messag
         * @param start the start waypoint
         * @param end the end waypoint
         */
        private RouteParameters(boolean valid, String error, Waypoint start, Waypoint end) {
            this.valid = valid;
            this.error = error;
            this.start = start;
            this.end = end;
        }

        /**
         * Checks if these parameters are valid and if not, IllegalStateException is thrown
         */
        private void checkValid() {
            if (!valid)
                throw new IllegalStateException("These parameters are invalid, you cannot access its waypoints");
        }

        /**
         * Determines if these parameters are valid or not
         * @return true if valid, false if not
         */
        public boolean isValid() {
            return valid;
        }

        /**
         * Retrieve the error message if {@link #isValid()} returns false
         * @return the error message
         */
        public String getError() {
            return error;
        }

        /**
         * Retrieve the start waypoint
         * @return the start waypoint
         * @throws IllegalStateException if {@link #isValid()} returns false
         */
        public Waypoint getStart() {
            checkValid();
            return start;
        }

        /**
         * Retrieve the end waypoint
         * @return the end waypoint
         * @throws IllegalStateException if {@link #isValid()} returns false
         */
        public Waypoint getEnd() {
            checkValid();
            return end;
        }

        /**
         * Creates an invalid instance of RouteParameters
         * @param error the error message
         * @return the created instance
         */
        protected static RouteParameters createInvalid(String error) {
            return new RouteParameters(false, error, null, null);
        }

        /**
         * Creates a valid instance of RouteParameters
         * @param start the start waypoint
         * @param end the end waypoint
         * @return the created instance
         */
        protected static RouteParameters createValid(Waypoint start, Waypoint end) {
            return new RouteParameters(true, null, start, end);
        }
    }
}
