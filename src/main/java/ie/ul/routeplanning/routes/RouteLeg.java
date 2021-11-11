package ie.ul.routeplanning.routes;

import ie.ul.routeplanning.routes.graph.Edge;
import ie.ul.routeplanning.routes.graph.GraphUtils;
import ie.ul.routeplanning.transport.TransportMethod;

import javax.persistence.*;
import java.util.Objects;

/**
 * A RouteLeg is a leg of a route with 2 waypoints representing the start of the leg and the end of it. It is also
 * composed of a transport type.
 *
 * It is important to note that while a user may expect there to be several waypoints on a leg of a route, this class is
 * an abstraction to the concept. RouteLeg just indicates the direction that would need to be travelled to get from start
 * to end. Any waypoints in between to navigate are out of scope of this system.
 */
@Entity
public class RouteLeg implements Edge {
	/**
	 * The id of this routeleg
	 */
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	/**
	 * The start of the RouteLeg
	 */
	@OneToOne(cascade=CascadeType.MERGE)
	private Waypoint start;
	/**
	 * The end of the RouteLeg
	 */
	@OneToOne(cascade=CascadeType.MERGE)
	private Waypoint end;
	/**
	 * The name of the transport method taking this leg of the route
	 */
	@OneToOne(cascade=CascadeType.MERGE)
	private TransportMethod transportMethod;
	/**
	 * A pre-defined distance for this edge. If null, the coordinate km distance will be used
	 */
	private Double distance;

	/**
	 * Construct a RouteLeg from the provided edge
	 * @param edge the edge to construct the route leg from
	 */
	public RouteLeg(Edge edge) {
		this(edge.getStart(), edge.getEnd(), edge.getTransportMethod(), edge.getDistance());
	}

	/**
	 * Constructs a RouteLeg with the provided start and end waypoints and the transport method and pre-defined distance
	 * Pre-conditions: The start and end waypoints cannot be null and cannot be equal to each other
	 * @param start the waypoint indicating the start of the leg
	 * @param end the waypoint indicating the end of the leg
	 * @param transportMethod the method of transport used to travel this leg
	 * @param distance the predefined distance for this route leg
	 */
	public RouteLeg(Waypoint start, Waypoint end, TransportMethod transportMethod, Double distance) {
		this.start = start;
		this.end = end;
		this.transportMethod = transportMethod;
		this.distance = distance;
	}

	/**
	 * Default constructor as required for Entity
	 */
	public RouteLeg() {
		this(null, null, null, null);
	}

	/**
	 * Constructs a RouteLeg with the provided start and end waypoints and the transport method
	 * Pre-conditions: The start and end waypoints cannot be null and cannot be equal to each other
	 * @param start the waypoint indicating the start of the leg
	 * @param end the waypoint indicating the end of the leg
	 * @param transportMethod the method of transport used to travel this leg
	 */
	public RouteLeg(Waypoint start, Waypoint end, TransportMethod transportMethod) {
		this(start, end, transportMethod, null);
	}

	/**
	 * Retrieves the start waypoint of this route leg
	 * @return the start waypoint
	 */
	public Waypoint getStart() {
		return start;
	}

	/**
	 * Sets the start waypoint of this route leg
	 * Pre-conditions: The waypoint must not be equal to the end waypoint and not be null
	 * @param start the new start waypoint
	 */
	public void setStart(Waypoint start) {
		this.start = start;
	}

	/**
	 * Retrieves the end waypoint of this route leg
	 * @return the end waypoint
	 */
	public Waypoint getEnd() {
		return end;
	}

	/**
	 * Sets the end waypoint of this route leg
	 * Pre-conditions: The waypoint must not be equal to the start waypoint and not be null
	 * @param end the end waypoint
	 */
	public void setEnd(Waypoint end) {
		this.end = end;
	}

	/**
	 * Retrieve the transport method used to travel this leg
	 * @return transport method
	 */
	public TransportMethod getTransportMethod() {
		return transportMethod;
	}

	/**
	 * Sets the transport method for this route leg
	 * @param transportMethod the new transport method
	 */
	public void setTransportMethod(TransportMethod transportMethod) {
		this.transportMethod = transportMethod;
	}

	/**
	 * Retrieves the pre-defined distance if any defined, or kilometre distance based on coordinates if not
	 * @return pre-defined distance in km
	 */
	@Override
	public Double getDistance() {
		return (this.distance == null) ? kilometreDistance():distance;
	}

	/**
	 * Sets the pre-defined distance in km
	 * @param distance pre-defined distance in km
	 */
	public void setDistance(Double distance) {
		this.distance = distance;
	}

	/**
	 * Calculates the distance between the waypoints of the leg
	 * @return the distance as a double
	 */
	public double calculateDistance() {
		return getDistance();
	}

	/**
	 * Calculates the CO2 emissions for this route
	 * @return the CO2 emissions for this route
	 */
	public double calculateCO2Emissions() {
		return transportMethod.getCO2EmissionsPerKm() * calculateDistance();
	}

	/**
	 * Calculate the distance from the start waypoint to end waypoint in kilometres by latitude and longitude
	 * @return calculated distance
	 */
	private double kilometreDistance() {
		return GraphUtils.kilometreDistance(start, end);
	}

	/**
	 * Calculate the time for this RouteLeg without any parameters
	 * @return the time as a double
	 */
	public double calculateTime() {
		double time = 0;
		// TODO calculate the time

		return time;
	}

	/**
	 * Returns an edge that is the reverse of this edge, i.e. a bidirectional version
	 *
	 * @return the bidirectional version of this edge
	 */
	@Override
	public Edge reverse() {
		return new RouteLeg(end, start, transportMethod, distance);
	}

	/**
	 * Check if this leg equals the provided object
	 * @param o the object to check
	 * @return true if equal, false if not
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RouteLeg routeLeg = (RouteLeg) o;
		return Objects.equals(id, routeLeg.id) &&
				Objects.equals(start, routeLeg.start) &&
				Objects.equals(end, routeLeg.end) &&
				Objects.equals(transportMethod, routeLeg.transportMethod) &&
				Objects.equals(distance, routeLeg.distance);
	}

	/**
	 * Generate a hash code for this route leg
	 * @return the generated hashcode
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id, start, end, transportMethod, distance);
	}
}
