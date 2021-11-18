package ie.ul.routeplanning.routes.graph.weights;

import ie.ul.routeplanning.routes.graph.Edge;

/**
 * This class represents a decorator that adds time onto the weight
 */
public class TimeWeightDecorator extends WeightDecorator {
    /**
     * Create a TimeWeightDecorator wrapping the provided weight function
     * @param weightFunction the weight function that is wrapped
     */
    public TimeWeightDecorator(WeightFunction weightFunction) {
        super(weightFunction);
    }

    /**
     * Calculate and return the weight for the edge with time added on
     *
     * @param edge the edge to calculate the weight for
     * @return the weight of this edge, i.e. the cost it takes to go from A to B via this edge
     */
    @Override
    public double calculate(Edge edge) {
        double kmHour = edge.getTransportMethod().getAverageSpeed();
        double factor = edge.getDistance() / kmHour;
        return weightFunction.calculate(edge) + factor;
    }
}
