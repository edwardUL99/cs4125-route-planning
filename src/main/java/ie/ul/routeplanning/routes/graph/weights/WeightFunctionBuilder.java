package ie.ul.routeplanning.routes.graph.weights;

/**
 * This class allows the building of the weight function with decorators if necessary.
 * The base function creates the base weight which is then modified depending on parameters passed into this builder.
 * The base is the weight based on km distance
 */
public class WeightFunctionBuilder {
    /**
     * Indicates if emissions wants to be added to the weights
     */
    private boolean emissions;
    /**
     * Indicates if time wants to be added to the weights
     */
    private boolean time;
    /**
     * The concrete implementation to use and it is defaulted
     */
    private WeightFunction weightFunction = new DistanceWeightFunction();

    /**
     * Determines if emissions wants to be added on to the weights
     * @param emissions true if emissions should be factored into the weights, false if not. If false, this is a no-op
     * but it allows taking request parameters that may be false but easier to pass in here. For example:
     *           if (emissions) {
     *              builder = builder.withEmissions(true);
     *           }
     * It's cleaner to just do:
     *            builder.withEmissions(emissions)....
     *
     * @return an instance of this to allow chaining
     */
    public WeightFunctionBuilder withEmissions(boolean emissions) {
        if (emissions) {
            weightFunction = new CO2WeightDecorator(weightFunction);
        }

        return this;
    }

    /**
     * Determines if time wants to be added on to the graph weights.
     * @param time true to add on time, false to not. If false, this is a no-op but it allows taking request parameters
     * that may be false but easier to pass in here. For example:
     *             if (timeRequired) {
     *                  builder = builder.withTime(true);
     *             }
     * It's cleaner to just do:
     *             builder.withTime(timeRequired)....
     *
     * @return an instance of this to allow chaining
     */
    public WeightFunctionBuilder withTime(boolean time) {
        if (time) {
            weightFunction = new TimeWeightDecorator(weightFunction);
        }

        return this;
    }

    /**
     * Build the weight function from the provided parameters. This is a terminal operation
     * @return the built weight function
     */
    public WeightFunction build() {
        WeightFunction weightFunction = this.weightFunction;

        this.weightFunction = new DistanceWeightFunction(); // reset the builder so it can be re-used

        return weightFunction;
    }
}
