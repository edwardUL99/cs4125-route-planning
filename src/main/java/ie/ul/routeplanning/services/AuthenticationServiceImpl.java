package ie.ul.routeplanning.services;

import ie.ul.routeplanning.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides an implementation of AuthenticationService
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    /**
     * The user service for working with users
     */
    private final UserService userService;
    /**
     * The security service for providing authentication details
     */
    private final SecurityService securityService;

    /**
     * Construct an AuthenticationService with the provided dependencies
     * @param userService the service for working with users
     * @param securityService the service for providing authentication details
     */
    @Autowired
    public AuthenticationServiceImpl(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    /**
     * Determines whether a user is currently logged in or not
     *
     * @return true if logged in (and not anonymous user) or false if not
     */
    @Override
    public boolean isAuthenticated() {
        return securityService.getUsername() != null;
    }

    /**
     * Get the user that is currently authenticated/logged in
     *
     * @return the logged in user, or null if not logged in or anonymous
     */
    @Override
    public User getAuthenticatedUser() {
        String username = securityService.getUsername();

        User user = null;

        if (username != null)
            user = userService.findByUsername(username);

        return user;
    }
}
