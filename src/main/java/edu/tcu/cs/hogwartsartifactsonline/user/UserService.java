package edu.tcu.cs.hogwartsartifactsonline.user;

import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<HogwartsUser> findAll() {
        return userRepository.findAll();
    }

    public HogwartsUser add(HogwartsUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public HogwartsUser findById(int userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("user", userId)
        );
    }

    public HogwartsUser update(int userId, HogwartsUser user) {
        HogwartsUser oldUser = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("user", userId)
        );
        oldUser.setUsername(user.getUsername());
        oldUser.setEnabled(user.isEnabled());
        oldUser.setRoles(user.getRoles());
        return userRepository.save(oldUser);
    }

    public void delete(int userId) {
        HogwartsUser userToBeDeleted = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("user", userId)
        );
        userRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HogwartsUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("username " + username + " not found")
        );
        return new MyUserPrincipal(user);
    }
}
