package edu.stage.backend.controller;

import edu.stage.backend.User;
import edu.stage.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
}

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        System.out.println("Email reçu : " + user.getEmail());

        
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            System.out.println("Conflit : l'utilisateur avec l'email existe déjà.");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

}
