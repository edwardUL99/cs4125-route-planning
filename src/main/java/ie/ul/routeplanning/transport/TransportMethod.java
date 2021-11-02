package ie.ul.routeplanning.transport;

import javax.persistence.*;
import java.time.Duration;
import java.util.Objects;

/**
 * This class represents a transport method.
 *
 * Would be implemented as an interface but cannot be persisted as an interface
 *
 * TODO may not need to be abstract. May just take a field for each of the abstract methods and instantiate from a json file
 * with each object being a transport method with the defined emissions, speed, time etc. Leave as abstract and sub-classed
 * for now
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public abstract class TransportMethod {
	/**
	 * The ID field of this transport method
	 */
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	/**
	 * The name of this transport method
	 */
	private String name;

	/**
	 * Creates a default TransportMethod
	 */
	public TransportMethod() {
		this(null, null);
	}

	/**
	 * Constructs a TransportMethod with the provided parameters
	 * @param id the ID for the TransportMethod
	 * @param name the name of the transport method
	 */
	public TransportMethod(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Retrieve this TransportMethod ID
	 * @return the id of the transport method
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id of this transport method
	 * @param id the transport method's id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Retrieve the name of this transport method
	 * @return the transport method's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of this transport method
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the average CO2 emissions per kilometre
	 * @return the average CO2 emissions per kilometre in grams
	 */
	public abstract double getCO2EmissionsPerKm();

	/**
	 * Gets the average duration per kilometre for this transport method
	 * @return the average duration it takes to travel a kilometre by this transport method
	 */
	public abstract Duration getTimePerKm();

	/**
	 * Gets the average speed per kilometre for this transport method
	 * @return average speed in km/h
	 */
	public abstract double getAverageSpeedPerKm();

	/**
	 * Checks if this TransportMethod equals the provided one
	 * @param o the provided one to check equality of
	 * @return true if equal, false if not
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TransportMethod)) return false;
		TransportMethod that = (TransportMethod) o;
		return Objects.equals(getId(), that.getId()) &&
				Objects.equals(getName(), that.getName()) &&
				Objects.equals(getCO2EmissionsPerKm(), that.getCO2EmissionsPerKm()) &&
				Objects.equals(getAverageSpeedPerKm(), that.getCO2EmissionsPerKm()) &&
				Objects.equals(getTimePerKm(), that.getTimePerKm());
	}

	/**
	 * Generates the hashcode for this transport method
	 * @return generated hashcode
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getId(), getName(), getCO2EmissionsPerKm(), getAverageSpeedPerKm(), getTimePerKm());
	}
}
