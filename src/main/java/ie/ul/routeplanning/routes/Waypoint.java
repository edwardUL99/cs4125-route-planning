package ie.ul.routeplanning.routes;

import javax.persistence.*;
import java.util.Objects;

/**
 * A Waypoint represents a set of coordinates locating the start and end of a route leg.
 * This allows the waypoint on a route to be saved and reloaded at the exact same position. If we relied on just
 * city name's for a waypoint, the results may vary on re-loading (i.e., the position of the actual waypoint may be
 * non-deterministic
 */
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = {"latitude", "longitude"})
})
public class Waypoint {
	/**
	 * The waypoint ID
	 */
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	/**
	 * The name assigned to this waypoint
	 */
	private String name;
	/**
	 * The latitude coordinate of the waypoint in degrees
	 */
	@Column(name="latitude")
	private Double latitude;
	/**
	 * The longitude coordinate of the waypoint in degrees
	 */
	@Column(name="longitude")
	private Double longitude;

	/**
	 * Constructs a Waypoint with the provided id, name and coordinates
	 * @param id the id for this waypoint
	 * @param name the name of the waypoint
	 * @param latitude the latitude of the waypoint's coordinates in degrees
	 * @param longitude the longitude of the waypoint's coordinates in degrees
	 */
	public Waypoint(Long id, String name, Double latitude, Double longitude) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Constructs a Waypoint with the provided name and coordinates
	 * @param name the name of the waypoint
	 * @param latitude the latitude of the waypoint's coordinates
	 * @param longitude the longitude of the waypoint's coordinates
	 */
	public Waypoint(String name, Double latitude, Double longitude) {
		this(null, name, latitude, longitude);
	}

	/**
	 * Constructs a default Waypoint with no parameters
	 */
	public Waypoint() {
		this(null, null, null, null);
	}

	/**
	 * Retrieve the ID for this waypoint
	 * @return waypoint ID
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Change the value of the ID for this waypoint
	 * @param id the new ID for the waypoint
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Retrieve the name of the waypoint
	 * @return waypoint name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the waypoint
	 * @param name the new waypoint name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retrieve the latitude of the waypoint in degrees
	 * @return waypoint latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Set the latitude of the waypoint in degrees
	 * @param latitude the new waypoint latitude
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Retrieve the longitude of the waypoint in degrees
	 * @return waypoint longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * Set the longitude of the waypoint in degrees
	 * @param longitude the new waypoint longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Returns true if this waypoint is the same as the provided waypoint based on the waypoints' coordinates
	 * Pre-conditions: Coordinates have either been set at construction time, or set with {@link #setLatitude(Double)}
	 * and {@link #setLongitude(Double)} methods. These coordinates must not be null
	 * @param waypoint the waypoint to check, must not be null
	 * @return true if this is the same waypoint as the the other based on coordinates
	 */
	public boolean isSameAs(Waypoint waypoint) {
		return latitude.equals(waypoint.latitude) && longitude.equals(waypoint.longitude);
	}

	/**
	 * Returns true if this waypoint equals another.
	 * Checks for equality on all fields.
	 * To check if the Waypoint has the same coordinates, see {@link #isSameAs(Waypoint)}
	 * @param o the instance of the object to check equality
	 * @return true if equal based on all fields, false if not
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Waypoint waypoint = (Waypoint) o;
		return Objects.equals(id, waypoint.id) &&
				Objects.equals(name, waypoint.name) &&
				Objects.equals(latitude, waypoint.latitude) &&
				Objects.equals(longitude, waypoint.longitude);
	}

	/**
	 * Generates the hashcode for this object
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id, name, latitude, longitude);
	}
}
