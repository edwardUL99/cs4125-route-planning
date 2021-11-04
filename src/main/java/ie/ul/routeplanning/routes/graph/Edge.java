package ie.ul.routeplanning.routes.graph;

import ie.ul.routeplanning.routes.Waypoint;
import ie.ul.routeplanning.transport.TransportMethod;

/**
 * This class represents an edge between A and B on a graph
 */
public interface Edge {
    /**
     * Get the start waypoint of this edge
     * @return start waypoint
     */
    Waypoint getStart();

    /**
     * Get the end waypoint of this edge
     * @return the end waypoint
     */
    Waypoint getEnd();

    /**
     * Get the transport method for this edge
     * @return the transport method travelling this edge
     */
    TransportMethod getTransportMethod();

    /**
     * Returns an edge that is the reverse of this edge, i.e. a bidirectional version
     * @return the bidirectional version of this edge
     */
    Edge reverse();

    /**
     * Retrieve the distance length of this edge
     * @return the length of the edge as KM distance
     */
    Double getDistance();
}
