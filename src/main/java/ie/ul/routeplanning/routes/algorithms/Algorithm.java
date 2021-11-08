package ie.ul.routeplanning.routes.algorithms;

import ie.ul.routeplanning.routes.graph.Graph;

/**
 * This interface represents an algorithm for processing a graph
 * @param <T> the type of the results of this algorithm
 */
public interface Algorithm<T> {
    /**
     * Perform the algorithm on the provided graph
     * @param graph the graph to perform the algorithm on
     * @return the result of the algorithm
     */
    Result<T> perform(Graph graph);
}
