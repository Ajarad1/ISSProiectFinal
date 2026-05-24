package map.teledon.server.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1. Extragem token-ul din cerere
            String jwt = parseJwt(request);

            // 2. Dacă token-ul există și este valid
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // 3. Aflăm cine e utilizatorul (username-ul)
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // 4. Încărcăm datele utilizatorului din baza de date
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. Creăm obiectul de autentificare și îl punem în Contextul de Securitate
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            System.err.println("Cannot set user authentication: " + e.getMessage());
        }

        // Dăm cererea mai departe în lanțul de filtre
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        // Token-ul vine de obicei sub forma "Bearer eyJhbGciOi..."
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Tăiem cuvântul "Bearer " ca să păstrăm doar token-ul
        }

        return null;
    }
}