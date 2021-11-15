package ie.ul.routeplanning.repositories;

import ie.ul.routeplanning.routes.Route;
import ie.ul.routeplanning.routes.SavedRoute;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * This represents a repository for saving and storing routed
 */
public interface RouteRepository extends CrudRepository<Route, Long> {
    /**
     * Finds all the saved routes by the provided username
     * @param username the username to find saved routes for
     * @return the list of saved routes
     */
    @Query("SELECT r FROM Route r JOIN User u ON r.user = u.id WHERE u.username = ?1 AND saved = True")
    List<SavedRoute> findSavedRoutesByUsername(String username);

    /**
     * Finds the saved route that corresponds with this username and route
     * @param username the username of the user that may have saved the route
     * @param routeId the ID of the route that may have been saved
     * @return the saved route instance or empty optional if not found
     */
    @Query("SELECT r FROM Route r JOIN User u ON r.user = u.id WHERE u.username = ?1 AND saved = True AND savedRoute = ?2")
    Optional<SavedRoute> findSavedRouteByUserAndRoute(String username, Long routeId);
}
