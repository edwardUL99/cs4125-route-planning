package ie.ul.routeplanning.routes.graph.creation;

import ie.ul.routeplanning.routes.graph.Graph;

/**
 * This interface represents an object that is able to construct a graph
 */
public interface GraphBuilder {
	/**
	 * Builds and returns the constructed graph
	 * @return the constructed graph
	 * @throws BuilderException if an error occurs building the graph
	 */
	Graph buildGraph() throws BuilderException;
}
