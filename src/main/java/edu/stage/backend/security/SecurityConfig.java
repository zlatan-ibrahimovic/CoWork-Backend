package edu.stage.backend.security;

import edu.stage.backend.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import jakarta.servlet.http.HttpServletResponse;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()  // 🔥 Authentification ouverte
                        .requestMatchers("/api/tasks/**").authenticated() // 🔒 Sécurisé
                        .requestMatchers("/api/users/register").permitAll()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // ✅ Gestion des erreurs
                .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((request, response, authException) -> {
                        System.out.println("❌ [AUTHENTICATION] Requête bloquée - 401 Unauthorized !");
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Non autorisé.");
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        System.out.println("❌ [ACCESS DENIED] Accès refusé - 403 Forbidden !");
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès refusé.");
                    })
                )
                .build();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(JwtUtil jwtUtil) {
        return new JwtAuthFilter(jwtUtil);
    }
}
