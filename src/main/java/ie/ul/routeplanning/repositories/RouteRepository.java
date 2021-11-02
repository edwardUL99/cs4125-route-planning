package ie.ul.routeplanning.repositories;

import ie.ul.routeplanning.routes.Route;
import org.springframework.data.repository.CrudRepository;

/**
 * This represents a repository for savign and storing routed
 */
public interface RouteRepository extends CrudRepository<Route, Long> {
}
