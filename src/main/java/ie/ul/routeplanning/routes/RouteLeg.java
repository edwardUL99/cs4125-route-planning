package ie.ul.routeplanning.routes;

import ie.ul.routeplanning.routes.graph.Edge;
import ie.ul.routeplanning.routes.graph.GraphUtils;
import ie.ul.routeplanning.parameters.Parameter;
import ie.ul.routeplanning.routes.graph.WeightFunction;
import ie.ul.routeplanning.transport.TransportMethod;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
	@OneToOne(cascade=CascadeType.ALL)
	private Waypoint start;
	/**
	 * The end of the RouteLeg
	 */
	@OneToOne(cascade=CascadeType.ALL)
	private Waypoint end;
	/**
	 * The name of the transport method taking this leg of the route
	 */
	@OneToOne(cascade=CascadeType.ALL)
	private TransportMethod transportMethod;
	/**
	 * A pre-defined distance for this edge. If null, the coordinate km distance will be used
	 */
	private Double distance;

	/**
	 * Constructs a RouteLeg with the provided start and end waypoints and the transport method and pre-defined distance
	 * @param start the waypoint indicating the start of the leg
	 * @param end the waypoint indicating the end of the leg
	 * @param transportMethod the method of transport used to travel this leg
	 * @param distance the predefined distance for this route leg
	 * @param validateWaypoints true if waypoint should be validated, false if not
	 */
	private RouteLeg(Waypoint start, Waypoint end, TransportMethod transportMethod, Double distance, boolean validateWaypoints) {
		if (validateWaypoints)
			validateWaypoints(start, end);

		this.start = start;
		this.end = end;
		this.transportMethod = transportMethod;
		this.distance = distance;
	}

	/**
	 * Construct a RouteLeg from the provided edge
	 * @param edge the edge to construct the route leg from
	 */
	public RouteLeg(Edge edge) {
		this(edge.getStart(), edge.getEnd(), edge.getTransportMethod(), edge.getDistance(), true);
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
		this(start, end, transportMethod, distance, true);
	}

	/**
	 * Default constructor as required for Entity
	 */
	public RouteLeg() {
		this(null, null, null, null, false);
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
	 * Checks if the waypoints are valid before setting them
	 * @param start the waypoint that will be the start waypoint
	 * @param end the waypoint that will be the end waypoint
	 */
	private void validateWaypoints(Waypoint start, Waypoint end) {
		if (start == null) {
			throw new IllegalStateException("A RouteLeg cannot have a null start Waypoint");
		} else if (end == null) {
			throw new IllegalStateException("A RouteLeg cannot have a null end Waypoint");
		} else if (start.isSameAs(end)) {
			throw new IllegalStateException("A RouteLeg cannot start and end with the same Waypoint");
		}
	}

	/**
	 * Sets the start waypoint of this route leg
	 * Pre-conditions: The waypoint must not be equal to the end waypoint and not be null
	 * @param start the new start waypoint
	 */
	public void setStart(Waypoint start) {
		validateWaypoints(start, end);
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
		validateWaypoints(start, end);
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
		return calculateDistance(new ArrayList<>());
	}

	/**
	 * Calculate the distance with the provided parameters
	 * @param parameters list of parameters to calculate distance with
	 * @return calculated distance
	 */
	public double calculateDistance(List<Parameter> parameters) {
		double distance = getDistance();

		for (Parameter parameter : parameters)
			distance += parameter.adjust(this);

		return distance;
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
		return calculateTime(new ArrayList<>());
	}

	/**
	 * Calculate the time for this RouteLeg using the parameters to adjust the time if necessary
	 * @param parameters any parameters to adjust the resulting time parameter
	 * @return the calculated time as a double
	 */
	public double calculateTime(List<Parameter> parameters) {
		double time = 0;
		//TODO: Calculate the time

		for (Parameter parameter : parameters)
			time += parameter.adjust(this);

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
}
