package ie.ul.routeplanning.services;

import ie.ul.routeplanning.repositories.TransportMethodRepository;
import ie.ul.routeplanning.repositories.WaypointRepository;
import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.routes.waypoints.SourceFactory;
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
    private WaypointRepository waypointRepository;
    /**
     * The repository for transport methods
     */
    private TransportMethodRepository transportMethodRepository;
    /**
     * The singleton graph instance which is lazily initialised by loadGraph
     */
    private static Graph GRAPH_INSTANCE = null;

    /**
     * Creates a GraphServiceImpl with the provided dependencies
     * @param waypointRepository the repository for loading waypoints
     * @param transportMethodRepository the repository for loading transport methods
     */
    @Autowired
    public GraphServiceImpl(WaypointRepository waypointRepository, TransportMethodRepository transportMethodRepository) {
        this.waypointRepository = waypointRepository;
        this.transportMethodRepository = transportMethodRepository;
    }

    /**
     * Loads the graph as a singleton instance and returns a copy of it.
     *
     * @return the loaded graph or a builder exception if it failed to be created
     * @throws BuilderException if an error occurred creating the graph
     */
    @Override
    public Graph loadGraph() throws BuilderException {
        if (GRAPH_INSTANCE == null) {
            Map<String, TransportMethod> transportMethodMap = new HashMap<>();

            transportMethodRepository.findAll().forEach(t -> transportMethodMap.put(t.getName(), t));

            List<Waypoint> waypoints = new ArrayList<>();
            waypointRepository.findAll().forEach(waypoints::add);
            GRAPH_INSTANCE = BuilderFactory.fromFile("edges.json",
                    SourceFactory.fromList(waypoints), transportMethodMap).buildGraph();
        }

        return GRAPH_INSTANCE.copy();
    }
}
