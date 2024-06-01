package edu.tcu.cs.hogwartsartifactsonline.user;

import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<HogwartsUser> findAll() {
        return userRepository.findAll();
    }

    public HogwartsUser add(HogwartsUser user) {
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
}
