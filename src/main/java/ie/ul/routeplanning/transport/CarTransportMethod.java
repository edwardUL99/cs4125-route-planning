package ie.ul.routeplanning.transport;

import javax.persistence.Entity;
import java.time.Duration;

/**
 * This represents a transport method for a car
 */
@Entity
public class CarTransportMethod extends TransportMethod {
	/**
	 * Creates a default CarTransportMethod
	 */
	public CarTransportMethod() {
	}

	/**
	 * Constructs a CarTransportMethod with the provided parameters
	 *
	 * @param id   the ID for the TransportMethod
	 * @param name the name of the transport method
	 */
	public CarTransportMethod(Long id, String name) {
		super(id, name);
	}

	/**
	 * Gets the average CO2 emissions per kilometre
	 *
	 * @return the average CO2 emissions per kilometre in grams
	 */
	@Override
	public double getCO2EmissionsPerKm() {
		return 122.0;
	}

	/**
	 * Gets the average duration per kilometre for this transport method
	 *
	 * @return the average duration it takes to travel a kilometre by this transport method
	 */
	@Override
	public Duration getTimePerKm() {
		// TODO implement
		return null;
	}

	/**
	 * Gets the average speed per kilometre for this transport method
	 *
	 * @return average speed in km/h
	 */
	@Override
	public double getAverageSpeedPerKm() {
		// TODO implement
		return 0;
	}
}
