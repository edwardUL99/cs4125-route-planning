package ie.ul.routeplanning.services;

import ie.ul.routeplanning.repositories.WaypointRepository;
import ie.ul.routeplanning.routes.Waypoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * An implementation of the WaypointService interface
 */
@Service
public class WaypointServiceImpl implements WaypointService {
    /**
     * The waypoint repository for accessing waypoints
     */
    private WaypointRepository waypointRepository;

    /**
     * Creates a WaypointServiceImpl with the provided dependencies
     * @param waypointRepository the waypoint repository for accessing waypoints
     */
    @Autowired
    public WaypointServiceImpl(WaypointRepository waypointRepository) {
        this.waypointRepository = waypointRepository;
    }

    /**
     * Finds the first waypoint with the provided name
     *
     * @param name the name of the waypoint to find
     * @return the first waypoint containing the provided name, null if none found
     */
    @Override
    public Waypoint findWaypoint(String name) {
        return waypointRepository.findByName(name)
                .stream()
                .findFirst()
                .orElse(null);
    }
}
