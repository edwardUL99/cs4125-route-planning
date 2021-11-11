package ie.ul.routeplanning.services;

/**
 * The security service provides automatic login functionality for a logged in user
 */
public interface SecurityService {
    /**
     * This method should return true if authentication has been granted
     * @return true if authenticated, false if not
     */
    boolean isAuthenticated();

    /**
     * This method attempts to automatically login the user with the provided username and password
     * @param username the username of the user to login
     * @param password the user's password
     */
    void autoLogin(String username, String password);

    /**
     * Gets the username of the current user logged in if any
     * @return the username of the current user logged in, null if not logged in or anonymous user
     */
    String getUsername();

    /**
     * Bypass authentication for testing purposes
     */
    boolean BYPASS_AUTH = System.getProperty("BYPASS_AUTH") != null;
}
