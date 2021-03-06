package ie.ul.routeplanning.transport;

import javax.persistence.Entity;

/**
 * This represents a transport method for a ferry
 */
@Entity
public class FerryTransportMethod extends TransportMethod {
	/**
	 * Creates a default FerryTransportMethod
	 */
	public FerryTransportMethod() {
	}

	/**
	 * Constructs a FerryTransportMethod with the provided parameters
	 *
	 * @param id   the ID for the TransportMethod
	 * @param name the name of the transport method
	 */
	public FerryTransportMethod(Long id, String name) {
		super(id, name);
	}

	/**
	 * Gets the average CO2 emissions per kilometre
	 *
	 * @return the average CO2 emissions per kilometre in grams
	 */
	@Override
	public double getCO2EmissionsPerKm() {
		return 19.0;
	}

	/**
	 * Gets the average speed per kilometre for this transport method
	 *
	 * @return average speed in km/h
	 */
	@Override
	public double getAverageSpeed() {
		return 37;
	}
}
