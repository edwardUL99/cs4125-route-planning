package ie.ul.routeplanning.routes.graph;

import java.time.Duration;

/**
 * This class represents a decorator that adds time onto the weight
 */
public class TimeWeightDecorator implements WeightFunction {
    /**
     * The weight function that is decorated
     */
    private WeightFunction weightFunction;

    /**
     * Create a TimeWeightDecorator wrapping the provided weight function
     * @param weightFunction the weight function that is wrapped
     */
    public TimeWeightDecorator(WeightFunction weightFunction) {
        this.weightFunction = weightFunction;
    }

    /**
     * Calculate and return the weight for the edge with time added on
     *
     * @param edge the edge to calculate the weight for
     * @return the weight of this edge, i.e. the cost it takes to go from A to B via this edge
     */
    @Override
    public double calculate(Edge edge) {
        // TODO this may not be correct
        Duration time = edge.getTransportMethod().getTimePerKm().multipliedBy(edge.getDistance().longValue());
        return weightFunction.calculate(edge) + time.toHours();
    }
}
