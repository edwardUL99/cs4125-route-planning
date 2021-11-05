package ie.ul.routeplanning.parameters;

import ie.ul.routeplanning.routes.RouteLeg;

import java.util.List;

/**
 * An interface that represents a parameter. A parameter works when calculating the edges of a graph by generating a
 * value that adjusts the weight.
 *
 * Graph weights are based on kilometre distance or time. However, with parameters added, this kilometre distance or time can be
 * adjusted to be longer or shorter. If adjusted to be shorter, to the Djikstra's algorithm, this route would be
 * preferred. If adjusted to be longer, the algorithm would treat this as the least preferred route
 */
@Deprecated
public interface Parameter {
    /**
     * Using the passed in edge, calculate the value that will be added to (positive value) or subtracted from (negative value)
     * the edge's weight.
     * @param routeLeg the leg that called the adjust method
     * @return the value to adjust the edge's weight by. It is added onto the weight so if you want to adjust by
     * subtracting, return a negative value
     */
    double adjust(RouteLeg routeLeg);
}
