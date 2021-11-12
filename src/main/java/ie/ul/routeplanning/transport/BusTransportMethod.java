package ie.ul.routeplanning.transport;

import javax.persistence.Entity;

/**
 * This represents a transport method for a bus
 */
@Entity
public class BusTransportMethod extends TransportMethod {
	/**
	 * Creates a default BusTransportMethod
	 */
	public BusTransportMethod() {
	}

	/**
	 * Constructs a BusTransportMethod with the provided parameters
	 *
	 * @param id   the ID for the TransportMethod
	 * @param name the name of the transport method
	 */
	public BusTransportMethod(Long id, String name) {
		super(id, name);
	}

	/**
	 * Gets the average CO2 emissions per kilometre
	 *
	 * @return the average CO2 emissions per kilometre in grams
	 */
	@Override
	public double getCO2EmissionsPerKm() {
		return 105.0;
	}

	/**
	 * Gets the average speed per kilometre for this transport method
	 *
	 * @return average speed in km/h
	 */
	@Override
	public double getAverageSpeed() {
		return 80;
	}
}
