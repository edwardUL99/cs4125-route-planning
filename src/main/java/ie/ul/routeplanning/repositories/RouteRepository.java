package ie.ul.routeplanning.repositories;

import ie.ul.routeplanning.routes.Route;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

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
    List<Route> findSavedRoutesByUsername(String username);
}
