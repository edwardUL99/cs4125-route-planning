package ie.ul.routeplanning.routes.graph.weights;

import ie.ul.routeplanning.routes.graph.Edge;

/**
 * This class represents a base decorator that other decorators decorating the WeightFunction can extend
 */
public abstract class WeightDecorator implements WeightFunction {
    /**
     * The decorated weight function
     */
    protected final WeightFunction weightFunction;

    /**
     * Construct the weight decorator with the provided weight function to decorate
     * @param weightFunction the weight function to decorate
     */
    public WeightDecorator(WeightFunction weightFunction) {
        this.weightFunction = weightFunction;
    }

    /**
     * This method should be overridden to use the decorated weight function and add extra functionality to it
     *
     * @param edge the edge to calculate the weight for
     * @return the weight of this edge, i.e. the cost it takes to go from A to B via this edge
     */
    @Override
    public abstract double calculate(Edge edge);
}
