package ie.ul.routeplanning.routes.graph;

/**
 * This class represents the simplest weight function, that is, using the distance as the weight
 */
public class DistanceWeightFunction implements WeightFunction {
    /**
     * Calculate the weight as the km distance of the edge
     * @param edge the edge to calculate the weight for
     * @return the km distance weight of the provided edge
     */
    public double calculate(Edge edge) {
        return edge.getDistance();
    }
}
