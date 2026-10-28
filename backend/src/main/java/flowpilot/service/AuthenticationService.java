package flowpilot.service;

import flowpilot.entity.User;
import flowpilot.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import flowpilot.security.JwtService;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public String register(String email, String password) {
        User user = new User(email, passwordEncoder.encode(password), "BUSINESS_OWNER");
        userRepository.save(user);
        return jwtService.generateToken(org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail()).password(user.getPassword()).authorities("ROLE_BUSINESS_OWNER").build());
    }

    public String login(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        User user = userRepository.findByEmail(email).orElseThrow();
        return jwtService.generateToken(org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail()).password(user.getPassword()).authorities("ROLE_BUSINESS_OWNER").build());
    }
}