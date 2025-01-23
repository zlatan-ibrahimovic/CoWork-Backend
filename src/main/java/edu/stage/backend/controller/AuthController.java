package edu.stage.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.stage.backend.User;
import edu.stage.backend.repository.UserRepository;
import edu.stage.backend.utils.JwtUtil;

import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        System.out.println("Requête reçue avec : Email = " + user.getEmail() + ", Password = " + user.getPassword());
        
        Optional<User> existingUserOpt = userRepository.findByEmail(user.getEmail());
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants invalides (email non trouvé)");
        }

        User existingUser = existingUserOpt.get();

        if (existingUser == null || !existingUser.getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants invalides");
        }
        
       
        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok("Token : " + token );
    }
}
