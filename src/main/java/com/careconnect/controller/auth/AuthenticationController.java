package com.careconnect.controller.auth;

import com.careconnect.model.dto.AuthResponseDTO;
import com.careconnect.model.dto.SigninRequestDTO;
import com.careconnect.model.dto.SignupRequestDTO;
import com.careconnect.model.entity.User;
import com.careconnect.repository.UserRepository;
import com.careconnect.service.auth.JWTTokenService;
import com.careconnect.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenService jwtTokenService;

    public AuthenticationController(UserService userService,
                                    UserRepository userRepository,
                                    PasswordEncoder passwordEncoder,
                                    JWTTokenService jwtTokenService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO req) {
        User u = new User(req.getNome(), req.getEmail(), req.getSenha(), req.getTelefone(), req.getTipo());
        u.setCpf(req.getCpf());
        u.setCnpj(req.getCnpj());

        User saved = userService.createUser(u); // faz hash e valida docs
        String access  = jwtTokenService.generateAccessToken(saved);
        String refresh = jwtTokenService.generateRefreshToken(saved);
        return ResponseEntity.ok(new AuthResponseDTO(access, refresh));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequestDTO req) {
        var user = userRepository.findByEmailAndAtivoTrue(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if (!passwordEncoder.matches(req.getSenha(), user.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        String access  = jwtTokenService.generateAccessToken(user);
        String refresh = jwtTokenService.generateRefreshToken(user);
        return ResponseEntity.ok(new AuthResponseDTO(access, refresh));
    }
}
