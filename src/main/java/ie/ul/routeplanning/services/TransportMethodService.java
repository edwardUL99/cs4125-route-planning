package ie.ul.routeplanning.services;

import ie.ul.routeplanning.transport.TransportMethod;

/**
 * This interface provides a service for interacting with TransportMethods
 */
public interface TransportMethodService {
    /**
     * Get the transport method with the provided name
     * @param name the name of the transport method
     * @return the transport method if found, null if not
     */
    TransportMethod getTransportMethod(String name);
}
