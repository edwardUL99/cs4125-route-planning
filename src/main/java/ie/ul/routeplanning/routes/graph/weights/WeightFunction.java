package ie.ul.routeplanning.routes.graph.weights;

import ie.ul.routeplanning.routes.graph.Edge;

/**
 * This interface represents a weight/cost function for an edge
 */
public interface WeightFunction {
    /**
     * Calculate and return the weight for the edge
     * @param edge the edge to calculate the weight for
     * @return the weight of this edge, i.e. the cost it takes to go from A to B via this edge
     */
    double calculate(Edge edge);
}
