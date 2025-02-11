package edu.stage.backend.security;

import edu.stage.backend.utils.JwtUtil;
import org.springframework.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println("🔎 JwtAuthFilter intercepte la requête pour : " + request.getRequestURI());
    
        String token = request.getHeader("Authorization");
    
        if (token == null || token.isEmpty()) {
            System.out.println("⚠️ Aucun token trouvé !");
            filterChain.doFilter(request, response);
            return;
        }
    
        if (!token.startsWith("Bearer ")) {
            System.out.println("❌ Token mal formé !");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token mal formé.");
            return;
        }
    
        token = token.substring(7);
        System.out.println("🛠 Token extrait : " + token);
    
        if (token.chars().filter(ch -> ch == '.').count() != 2) {
            System.out.println("❌ Token invalide : mauvais format !");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide : mauvais format.");
            return;
        }
    
        String email = jwtUtil.extractEmail(token);
        System.out.println("📩 Email extrait du token : " + email);
    
        if (email != null && jwtUtil.validateToken(token, email)) {
            System.out.println("✅ Token valide, authentification en cours...");
    
            User user = new User(email, "", Collections.emptyList());
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            System.out.println("❌ Token invalide ou expiré !");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide ou expiré.");
            return;
        }
    
        filterChain.doFilter(request, response);
    }
    
}
