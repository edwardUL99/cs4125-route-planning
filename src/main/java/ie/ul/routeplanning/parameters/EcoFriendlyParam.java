package ie.ul.routeplanning.parameters;

import ie.ul.routeplanning.routes.RouteLeg;
import ie.ul.routeplanning.transport.TransportMethod;

/**
 * This class represents an Eco Friendly parameter to filter the best routes by eco-friendliness
 */
public class EcoFriendlyParam implements Parameter {
    /**
     * Adjusts the route leg's weighting based on eco-friendliness
     * @param routeLeg the leg that called the adjust method
     * @return the adjustment value
     */
    public double adjust(RouteLeg routeLeg)    {
        TransportMethod transportMethod = routeLeg.getTransportMethod();
        return transportMethod.getCO2EmissionsPerKm() * routeLeg.calculateDistance();
    }
}
