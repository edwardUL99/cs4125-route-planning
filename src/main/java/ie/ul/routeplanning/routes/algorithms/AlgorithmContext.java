package ie.ul.routeplanning.routes.algorithms;

import ie.ul.routeplanning.routes.graph.Graph;

/**
 * This class provides the context for working with the algorithms package.
 * It provides the "context" component of the Strategy pattern
 * @param <T> the type of algorithm this context is working on
 */
public class AlgorithmContext<T> {
	/**
	 * The algorithm the context will delegate to
	 */
	private Algorithm<T> algorithm;
	/**
	 * The graph the context will delegate to
	 */
	private Graph graph;

	/**
	 * Create a default context
	 */
	public AlgorithmContext() {
		this(null, null);
	}

	/**
	 * Create an AlgorithmContext with the provided algorithm and graph
	 * @param algorithm the algorithm the context will operate with
	 * @param graph the graph to delegate the algorithm call on
	 */
	public AlgorithmContext(Algorithm<T> algorithm, Graph graph) {
		this.algorithm = algorithm;
		this.graph = graph;
	}

	/**
	 * Set the algorithm to use on the graph
	 * @param algorithm the algorithm to delegate to
	 */
	public void setAlgorithm(Algorithm<T> algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * Set the graph to pass to the algorithm
	 * @param graph the graph to pass into the algorithm
	 */
	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	/**
	 * Resets the context to an empty context, i.e. algorithm and graph is null afterwards
	 */
	public void reset() {
		algorithm = null;
		graph = null;
	}

	/**
	 * Validates that neither the algorithm or graph is null
	 */
	private void validate() {
		if (algorithm == null)
			throw new IllegalStateException("The algorithm cannot be null. Call setAlgorithm with a non-null algorithm");

		if (graph == null)
			throw new IllegalStateException("The graph cannot be null. Call setGraph with a non-null graph");
	}

	/**
	 * Perform the operation provided by the given algorithm on the set graph
	 * @return the result of the operation
	 * @throws IllegalStateException if either algorithm or graph is null
	 */
	public Result<T> perform() {
		validate();
		return algorithm.perform(graph);
	}
}
