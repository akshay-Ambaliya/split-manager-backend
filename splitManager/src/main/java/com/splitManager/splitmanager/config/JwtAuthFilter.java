package com.splitManager.splitmanager.config;

import com.splitManager.splitmanager.entity.User;
import com.splitManager.splitmanager.exception.AuthException;
import com.splitManager.splitmanager.repository.UserRepository;
import com.splitManager.splitmanager.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtService;
    private final UserRepository userRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {

            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                sendError(response, HttpStatus.UNAUTHORIZED, "JWT token is missing");
                return;
            }

            String token = authHeader.substring(7);

            // 🔥 SINGLE POINT OF FAILURE HANDLING
            if (jwtService.isTokenExpired(token)) {
                sendError(response, HttpStatus.UNAUTHORIZED, "Token expired");
                return;
            }

            if (!jwtService.isValid(token)) {
                sendError(response, HttpStatus.UNAUTHORIZED, "Invalid JWT token");
                return;
            }

            String email = jwtService.extractUsername(token);

            if (email == null) {
                sendError(response, HttpStatus.UNAUTHORIZED, "Invalid token payload");
                return;
            }

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new AuthException("User not found",HttpStatus.BAD_REQUEST));

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            Collections.emptyList()
                    );

            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            log.error("JWT Filter error", ex);
            sendError(response, HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        }
    }

    private void sendError(HttpServletResponse response,
                           HttpStatus status,
                           String message) throws IOException {

        response.setStatus(status.value());
        response.setContentType("application/json");

        response.getWriter().write(
                String.format("{\"success\": false, \"message\": \"%s\"}", message)
        );
    }
}