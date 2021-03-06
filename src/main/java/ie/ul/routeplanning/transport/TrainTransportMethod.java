package ie.ul.routeplanning.transport;

import javax.persistence.Entity;

/**
 * This represents a transport method for a train
 */
@Entity
public class TrainTransportMethod extends TransportMethod {
	/**
	 * Creates a default TrainTransportMethod
	 */
	public TrainTransportMethod() {
	}

	/**
	 * Constructs a TrainTransportMethod with the provided parameters
	 *
	 * @param id   the ID for the TransportMethod
	 * @param name the name of the transport method
	 */
	public TrainTransportMethod(Long id, String name) {
		super(id, name);
	}

	/**
	 * Gets the average CO2 emissions per kilometre
	 *
	 * @return the average CO2 emissions per kilometre in grams
	 */
	@Override
	public double getCO2EmissionsPerKm() {
		return 41.0;
	}

	/**
	 * Gets the average speed per kilometre for this transport method
	 *
	 * @return average speed in km/h
	 */
	@Override
	public double getAverageSpeed() {
		return 65;
	}
}
