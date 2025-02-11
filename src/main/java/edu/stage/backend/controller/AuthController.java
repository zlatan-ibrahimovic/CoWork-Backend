package edu.stage.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.stage.backend.model.User;
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Identifiants invalides (email non trouvé)"));
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
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide");
        }

        String email;
        try {
            email = jwtUtil.extractEmail(token);
            if (!jwtUtil.validateToken(token, email)) { // Ajout de l'email
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide");
            }
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
    public ResponseEntity<String> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                            @RequestBody(required = false) Map<String, String> request) {
    
        String token = null;

        // 1️⃣ Vérifier si le token est dans le header Authorization
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
    
        // 2️⃣ Si aucun token trouvé dans le header, vérifier dans le body
        if (token == null && request != null) {
            token = request.get("token");
        }

        // 3️⃣ Vérifier que le token est bien présent
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Token manquant ou vide.");
        }

        String email = jwtUtil.extractEmail(token);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Impossible d'extraire l'email du token.");
        }

        boolean isValid = jwtUtil.validateToken(token, email);

        if (isValid) {
            return ResponseEntity.ok("✅ Le token est valide.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Le token est invalide ou expiré.");
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token invalide ou manquant.");
        }

        token = token.substring(7); // Retirer "Bearer "

        String email = jwtUtil.extractEmail(token);
        if (!jwtUtil.validateToken(token, email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide.");
        }

        // Ajouter le token à la blacklist
        jwtUtil.blacklistToken(token);
        System.out.println("🚫 Token ajouté à la blacklist : " + token);

        return ResponseEntity.ok("Déconnexion réussie, token invalidé.");
    }

}