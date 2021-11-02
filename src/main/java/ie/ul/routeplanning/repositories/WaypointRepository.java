package ie.ul.routeplanning.repositories;


import ie.ul.routeplanning.routes.Waypoint;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * This interface represents the repository for accessing waypoints
 */
public interface WaypointRepository extends CrudRepository<Waypoint, Long> {
    List<Waypoint> findByName(String name);
}
