package edu.stage.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import edu.stage.backend.utils.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // 🔥 Authentification ouverte
                        .requestMatchers("/api/tasks/**").authenticated() // 🔒 Sécurisé
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                
                // ✅ Gestion des erreurs pour retourner 401 Unauthorized
                .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((request, response, authException) -> {
                        System.out.println("❌ [AUTHENTICATION] Requête bloquée - 401 Unauthorized !");
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Non autorisé.");
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        System.out.println("❌ [ACCESS DENIED] Accès refusé - 403 Forbidden !");
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Accès refusé.");
                    })
                )
                .build();
    }
    
    @Bean
    public JwtAuthFilter jwtAuthFilter(JwtUtil jwtUtil) {
        return new JwtAuthFilter(jwtUtil);
}

}
