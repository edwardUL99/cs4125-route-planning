package ie.ul.routeplanning.services;

import ie.ul.routeplanning.repositories.TransportMethodRepository;
import ie.ul.routeplanning.repositories.WaypointRepository;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.data.SourceFactory;
import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.routes.graph.creation.BuilderException;
import ie.ul.routeplanning.routes.graph.creation.BuilderFactory;
import ie.ul.routeplanning.transport.TransportMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is an implementation of the GraphService interface
 */
@Service
public class GraphServiceImpl implements GraphService {
    /**
     * Our repository for finding waypoints
     */
    @Autowired
    private WaypointRepository waypointRepository;
    /**
     * The repository for transport methods
     */
    @Autowired
    private TransportMethodRepository transportMethodRepository;

    /**
     * Loads the graph and returns it
     *
     * @return the loaded graph or a builder exception if it failed to be created
     * @throws BuilderException if an error occurred creating the graph
     */
    @Override
    public Graph loadGraph() throws BuilderException {
        Map<String, TransportMethod> transportMethodMap = new HashMap<>();

        transportMethodRepository.findAll().forEach(t -> transportMethodMap.put(t.getName(), t));

        List<Waypoint> waypoints = new ArrayList<>();
        waypointRepository.findAll().forEach(waypoints::add);
        return BuilderFactory.fromFile("edges.json", SourceFactory.fromList(waypoints), transportMethodMap).buildGraph();
    }
}
