package ie.ul.routeplanning.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * The concrete implementation for the security service
 */
@Service
public class SecurityServiceImpl implements SecurityService{
    /**
     * The authentication manager for the security service
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * The user details service for the security service
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * This method should return true if authentication has been granted
     *
     * @return true if authenticated, false if not
     */
    @Override
    public boolean isAuthenticated() {
        if (BYPASS_AUTH)
            return true;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            // either if authentication is null or we are using anonymous authentication, either way, if this occurs, we are not authenticated
            return false;
        }

        return authentication.isAuthenticated(); // delegate to Authentication's isAuthenticated method
    }

    /**
     * This method attempts to automatically login the user with the provided username and password
     *
     * @param username the username of the user to login
     * @param password the user's password
     */
    @Override
    public void autoLogin(String username, String password) {
        if (System.getProperty("BYPASS_AUTH") != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

            authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            if (usernamePasswordAuthenticationToken.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
    }

    /**
     * Gets the username of the current user logged in if any
     *
     * @return the username of the current user logged in, null if not logged in or anonymous user
     */
    @Override
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            String username = (principal instanceof UserDetails) ? ((UserDetails)principal).getUsername():principal.toString();

            return (username.equals("anonymousUser")) ? null:username;
        } else {
            return null;
        }
    }
}
