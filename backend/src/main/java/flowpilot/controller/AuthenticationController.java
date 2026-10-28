package flowpilot.controller;

import flowpilot.service.AuthenticationService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> request) {
        String token = authService.register(request.get("email"), request.get("password"));
        return Map.of("token", token);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String token = authService.login(request.get("email"), request.get("password"));
        return Map.of("token", token);
    }
}