package ie.ul.routeplanning.services;

import ie.ul.routeplanning.repositories.UserRepository;
import ie.ul.routeplanning.users.Role;
import ie.ul.routeplanning.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The implementation of a service for User details for our project
 */
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    /**
     * Our user repository for retrieving users
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Load the user with the provided username
     * @param username the username of the user
     * @return the user details for the user if found
     * @throws UsernameNotFoundException if no user with the provided username is found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) throw new UsernameNotFoundException(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
