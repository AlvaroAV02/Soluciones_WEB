package com.upn.ecommerce.controller;

import com.upn.ecommerce.dto.AuthDTOs.*;
import com.upn.ecommerce.entity.Usuario;
import com.upn.ecommerce.repository.UsuarioRepository;
import com.upn.ecommerce.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(AuthenticationManager authManager, UsuarioRepository usuarios,
                          PasswordEncoder encoder, JwtService jwt) {
        this.authManager = authManager;
        this.usuarios = usuarios;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest req) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }
        Usuario u = usuarios.findByUsername(req.username()).orElseThrow();
        return new TokenResponse(jwt.generar(u.getUsername(), u.getRol()), "Bearer", u.getUsername(), u.getRol());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse register(@Valid @RequestBody RegisterRequest req) {
        if (usuarios.existsByUsername(req.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        }
        Usuario u = usuarios.save(new Usuario(req.username(), encoder.encode(req.password()), "USER"));
        return new TokenResponse(jwt.generar(u.getUsername(), u.getRol()), "Bearer", u.getUsername(), u.getRol());
    }
}
