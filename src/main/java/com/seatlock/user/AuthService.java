package com.seatlock.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.seatlock.common.exception.InvalidCredentialsException;
import com.seatlock.common.security.JwtService;
import com.seatlock.user.dto.LoginRequest;
import com.seatlock.user.dto.LoginResponse;
import com.seatlock.user.dto.RegisterRequest;
import com.seatlock.user.dto.RegisterResponse;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,PasswordEncoder passwordEncoder , JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("Email already exists");
        }

        User user = new User();

        user.setEmail(request.email());
        user.setPassword(
                passwordEncoder.encode(request.password())
        );
        user.setRole("USER");

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
             savedUser.getId(),
             savedUser.getEmail(),
             savedUser.getRole()
        );
    }

    public LoginResponse login(LoginRequest request) {

    User user = userRepository.findByEmail(request.email())
            .orElseThrow(InvalidCredentialsException::new);

    boolean passwordMatches = passwordEncoder.matches(
            request.password(),
            user.getPassword()
    );

    if (!passwordMatches) {
        throw new InvalidCredentialsException();
    }

    String token = jwtService.generateToken(user);

    return new LoginResponse(
            user.getId(),
            user.getEmail(),
            user.getRole(),
            token
    );
}
}