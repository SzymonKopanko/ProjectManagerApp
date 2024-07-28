package skopanko.projectmanagerapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import skopanko.projectmanagerapi.dto.UserDTO;
import skopanko.projectmanagerapi.entity.AppUser;
import skopanko.projectmanagerapi.mapper.UserMapper;
import skopanko.projectmanagerapi.service.UserService;

import java.util.Base64;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        AppUser user = UserMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        AppUser savedUser = userService.saveUser(user);
        String token = Base64.getEncoder().encodeToString((userDTO.getUsername() + ":" + userDTO.getPassword()).getBytes());
        return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO.getUsername(), userDTO.getPassword())
                .map(user -> {
                    String token = Base64.getEncoder().encodeToString((userDTO.getUsername() + ":" + userDTO.getPassword()).getBytes());
                    return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
                })
                .orElse(ResponseEntity.status(401).build());
    }
}
