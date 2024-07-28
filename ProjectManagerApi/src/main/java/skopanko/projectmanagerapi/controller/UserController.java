package skopanko.projectmanagerapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import skopanko.projectmanagerapi.dto.UserDTO;
import skopanko.projectmanagerapi.entity.AppUser;
import skopanko.projectmanagerapi.mapper.UserMapper;
import skopanko.projectmanagerapi.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);


    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.findAllUsers().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.findUserById(id)
                .map(UserMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, Authentication authentication) {
        AppUser loggedInUser = userService.findUserByUsername(authentication.getName());
        loggedInUser.setUsername(userDTO.getUsername());
        loggedInUser.setEmail(userDTO.getEmail());
        if (userDTO.getPassword() != null) {
            loggedInUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        AppUser updatedUser = userService.saveUser(loggedInUser);
        return ResponseEntity.ok(UserMapper.toDto(updatedUser));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(Authentication authentication) {
        AppUser loggedInUser = userService.findUserByUsername(authentication.getName());
        userService.deleteUser(loggedInUser.getId());
        return ResponseEntity.ok().build();
    }
}
