package ie.ul.routeplanning.repositories;

import ie.ul.routeplanning.users.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * This represents an interface for storing roles in the system
 */
public interface RoleRepository extends CrudRepository<Role, Long> {
    /**
     * Find roles with the provided name
     * @param name the name of the role
     * @return a list of found roles if any
     */
    List<Role> findByName(String name);
}
