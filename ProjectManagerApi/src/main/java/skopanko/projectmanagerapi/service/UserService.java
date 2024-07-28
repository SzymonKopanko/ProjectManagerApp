package skopanko.projectmanagerapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import skopanko.projectmanagerapi.entity.AppUser;
import skopanko.projectmanagerapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<AppUser> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<AppUser> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public AppUser findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public AppUser saveUser(AppUser user) {
        return userRepository.save(user);
    }

    public Optional<AppUser> login(String username, String password) {
        Optional<AppUser> user = userRepository.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    @Transactional
    public AppUser updateUser(AppUser user) {
        return userRepository.save(user);
    }

    // Usuń użytkownika
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public AppUser findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<AppUser> findAppUsersByProjectId(Long projectId) {
        return userRepository.findAllUsersByProjectId(projectId);
    }

    public List<AppUser> findAppUsersByTaskId(Long taskId) {
        return userRepository.findAllUsersByTaskId(taskId);
    }
}
