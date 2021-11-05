package ie.ul.routeplanning.services;

import ie.ul.routeplanning.users.User;

/**
 * This interface provides a service for creating users
 */
public interface UserService {
    /**
     * Save the user to the system
     * @param user the user to save
     */
    void save(User user);

    /**
     * Find the user with the given username
     * @param username the username to find the user by
     * @return the user if found, null if not
     */
    User findByUsername(String username);

    /**
     * Find the user by the given email address
     * @param email the email to find the user by
     * @return the user if found, null if not
     */
    User findByEmail(String email);
}
