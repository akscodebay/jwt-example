package org.jwt.jwtexample.authcontrollers;

import org.jwt.jwtexample.util.JwtUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtUtil jwtUtil, @Lazy AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/authenticate")
    public Map<String, String> getJwtToken(@RequestParam String username,@RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        User user = (User) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", user.getAuthorities());
        String token = jwtUtil.generateToken(username, claims);
        return Map.of("token_id", token);
    }

    @GetMapping("/hello")
    public String getHello(@AuthenticationPrincipal User user) {
        return "Hello " + user.getUsername();
    }

}
