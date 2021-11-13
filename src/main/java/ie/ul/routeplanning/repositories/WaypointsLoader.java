package ie.ul.routeplanning.repositories;

import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.waypoints.SourceFactory;
import ie.ul.routeplanning.routes.waypoints.WaypointSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * This class loads in the waypoints into the waypoint repository on startup
 */
@Component
public class WaypointsLoader implements CommandLineRunner {
    /**
     * Our waypoint repository to dump data into
     */
    private final WaypointRepository waypointRepository;

    /**
     * Creates an instance of this WaypointsLoader
     * @param waypointRepository the repository to dump waypoints into
     */
    @Autowired
    public WaypointsLoader(WaypointRepository waypointRepository) {
        this.waypointRepository = waypointRepository;
    }

    /**
     * Runs this component
     * @param args the arguments to pass to the method
     * @throws Exception if an error occurs
     */
    @Override
    public void run(String... args) throws Exception {
        WaypointSource source = SourceFactory.fromFile("waypoints.json");

        Consumer<Waypoint> saveIfNotExists = w -> waypointRepository.findById(w.getId())
        .orElseGet(() -> waypointRepository.save(w));

        source.getWaypoints().forEach(saveIfNotExists);
    }
}
