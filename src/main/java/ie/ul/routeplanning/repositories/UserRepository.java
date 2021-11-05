package ie.ul.routeplanning.repositories;

import ie.ul.routeplanning.users.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * This represents the repository for storing users in the system
 */
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Find the user by their username. Username is a unique key, so you are guaranteed to get only one user
     * @param username the username to find the username by
     * @return the user if found or an empty optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Find the user by their email. Email is a unique field so you are guaranteed that you will find at most 1 user
     * @param email the email address to find the user by
     * @return the user if found or an empty optional
     */
    Optional<User> findByEmail(String email);
}
