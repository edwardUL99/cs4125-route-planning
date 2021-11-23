package ie.ul.routeplanning.services;

import ie.ul.routeplanning.users.User;

/**
 * This interface provides functionality for merging security service into user service to provide a complete
 * authentication flow.
 *
 * This has been provided to reduce the complexity of checking if a user is authenticated and then retrieving that user
 * from the database. Before, this would have required using both the SecurityService and UserService. However, in cases
 * like the RouteController, some of its methods only cared about checking authentication and retrieving the authenticated
 * user and not having to deal with 2 different services for one task. This interface has been designed to separate the concern
 * from the route controller to this by delegating to the SecurityService and UserService on behalf of the clients.
 *
 * Note that this interface does not provide login functionality or functionality for saving new users.
 */
public interface AuthenticationService {
    /**
     * Determines whether a user is currently logged in or not
     * @return true if logged in (and not anonymous user) or false if not
     */
    boolean isAuthenticated();

    /**
     * Get the user that is currently authenticated/logged in
     * @return the logged in user, or null if not logged in or anonymous
     */
    User getAuthenticatedUser();
}
