package ie.ul.routeplanning.repositories;

import ie.ul.routeplanning.transport.TransportMethod;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A repository for saving and retrieving transport method information
 */
@Repository
public interface TransportMethodRepository extends CrudRepository<TransportMethod, Long> {
    /**
     * Find the transport method by name
     * @param name the name of the transport method
     * @return an optional possibly containing the found transport method
     */
    Optional<TransportMethod> findByName(String name);
}
