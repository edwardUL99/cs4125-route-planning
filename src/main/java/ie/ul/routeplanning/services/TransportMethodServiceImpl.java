package ie.ul.routeplanning.services;

import ie.ul.routeplanning.repositories.TransportMethodRepository;
import ie.ul.routeplanning.transport.TransportMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is the implementation of the transport method service
 */
@Service
public class TransportMethodServiceImpl implements TransportMethodService {
    /**
     * The repository for storing transport methods
     */
    @Autowired
    private TransportMethodRepository transportMethodRepository;

    /**
     * Get the transport method with the provided name
     *
     * @param name the name of the transport method
     * @return the transport method if found, null if not
     */
    @Override
    public TransportMethod getTransportMethod(String name) {
        return transportMethodRepository.findByName(name).orElse(null);
    }
}
