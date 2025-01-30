package edu.stage.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.stage.backend.User;
import edu.stage.backend.repository.UserRepository;
import edu.stage.backend.utils.JwtUtil;

import java.util.Optional;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        System.out.println("Requête reçue avec : Email = " + user.getEmail() + ", Password = " + user.getPassword());
        
        Optional<User> existingUserOpt = userRepository.findByEmail(user.getEmail());
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Identifiants invalides (email non trouvé)"));
        }

        User existingUser = existingUserOpt.get();

        if (existingUser == null || !existingUser.getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Identifiants invalides"));
        }
        
       
        String token = jwtUtil.generateToken(user.getEmail());

        System.out.println("Token généré pour l'utilisateur : " + token);

        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/getUserByToken")
    public ResponseEntity<?> getUserByToken(@RequestHeader("Authorization") String token) {
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide");
        }

        String email;
        try {
            email = jwtUtil.extractEmail(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide");
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
        }

        return ResponseEntity.ok(userOpt.get());
    }

    @PostMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestBody String token) {
        try {
            boolean isValid = jwtUtil.validateToken(token);

            if (isValid) {
                return ResponseEntity.ok("Le token est valide.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Le token est invalide.");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la validation du token : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la validation du token.");
        }
    }

}

