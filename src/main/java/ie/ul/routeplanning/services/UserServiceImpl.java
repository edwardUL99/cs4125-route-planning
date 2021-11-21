package ie.ul.routeplanning.services;

import ie.ul.routeplanning.constants.Constant;
import ie.ul.routeplanning.repositories.RoleRepository;
import ie.ul.routeplanning.repositories.UserRepository;
import ie.ul.routeplanning.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * This is the implementation of the user service
 */
@Service
public class UserServiceImpl implements UserService {
    /**
     * The user repository to delegate to
     */
    private final UserRepository userRepository;
    /**
     * The role repository to delegate to
     */
    private final RoleRepository roleRepository;
    /**
     * The password encoder for password encryption
     */
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Create a UserServiceImpl with the provided dependencies
     * @param userRepository the user repository for saving users
     * @param roleRepository the repository for saving user roles
     * @param bCryptPasswordEncoder the password encoder for password encryption
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Save the user to the system
     *
     * @param user the user to save
     */
    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(Constant.iterableToSet(roleRepository.findAll()));
        userRepository.save(user); // delegate to the user repository save
    }

    /**
     * Find the user with the given username
     *
     * @param username the username to find the user by
     * @return the user if found, null if not
     */
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * Find the user by the given email address
     *
     * @param email the email to find the user by
     * @return the user if found, null if not
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
