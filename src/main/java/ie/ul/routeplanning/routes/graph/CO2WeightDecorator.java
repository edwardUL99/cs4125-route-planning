package ie.ul.routeplanning.routes.graph;

/**
 * This class is a decorator for adding CO2 emissions to the weight
 */
public class CO2WeightDecorator implements WeightFunction {
    /**
     * The decorated weight function
     */
    private final WeightFunction weightFunction;

    /**
     * Create a C02WeightDecorator which decorates the provided weight function
     * @param weightFunction the weight function to decorate
     */
    public CO2WeightDecorator(WeightFunction weightFunction) {
        this.weightFunction = weightFunction;
    }

    /**
     * Calculate and return the weight for the edge with CO2 emissions added on
     *
     * @param edge the edge to calculate the weight for
     * @return the weight of this edge, i.e. the cost it takes to go from A to B via this edge
     */
    @Override
    public double calculate(Edge edge) {
        double emissions = edge.getTransportMethod().getCO2EmissionsPerKm();
        return weightFunction.calculate(edge) + emissions;
    }
}
