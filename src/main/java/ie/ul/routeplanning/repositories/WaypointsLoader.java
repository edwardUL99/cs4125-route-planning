package ie.ul.routeplanning.repositories;

import ie.ul.routeplanning.routes.data.SourceFactory;
import ie.ul.routeplanning.routes.data.WaypointSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * This class loads in the waypoints into the waypoint repository on startup
 */
@Component
public class WaypointsLoader implements CommandLineRunner {
    private final WaypointRepository waypointRepository;

    @Autowired
    public WaypointsLoader(WaypointRepository waypointRepository) {
        this.waypointRepository = waypointRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        WaypointSource source = SourceFactory.fromFile("waypoints.json");
        waypointRepository.saveAll(source.getWaypoints());
    }
}
