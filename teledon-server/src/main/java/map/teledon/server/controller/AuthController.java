package map.teledon.server.controller;

import map.teledon.server.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/teledon/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        try {
            // 1. Spring Security verifică dacă username-ul și parola există și sunt corecte în baza de date
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.get("username"),
                            loginRequest.get("password")
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 2. Dacă sunt corecte, generăm token-ul
            String jwt = jwtUtils.generateJwtToken(authentication);

            // 3. Returnăm token-ul către React (împreună cu numele voluntarului)
            return ResponseEntity.ok(Map.of(
                    "token", jwt,
                    "username", loginRequest.get("username")
            ));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Autentificare eșuată! Credențiale incorecte.");
        }
    }
}