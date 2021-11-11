package ie.ul.routeplanning.services;

import ie.ul.routeplanning.routes.graph.Graph;
import ie.ul.routeplanning.routes.graph.creation.BuilderException;

/**
 * This service provides a service for loading and returning a graph
 */
public interface GraphService {
    /**
     * Loads the graph as a singleton instance and returns a copy of it.
     *
     * @return the loaded graph
     * @throws BuilderException if an error occurred creating the graph
     */
    Graph loadGraph() throws BuilderException;
}
