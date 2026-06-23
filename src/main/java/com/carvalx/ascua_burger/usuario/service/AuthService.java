package com.carvalx.ascua_burger.usuario.service;

import com.carvalx.ascua_burger.security.JwtService;
import com.carvalx.ascua_burger.usuario.domain.Rol;
import com.carvalx.ascua_burger.usuario.domain.Usuario;
import com.carvalx.ascua_burger.usuario.dto.LoginRequest;
import com.carvalx.ascua_burger.usuario.dto.RegisterRequest;
import com.carvalx.ascua_burger.usuario.dto.TokenResponse;
import com.carvalx.ascua_burger.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .rol(Rol.CLIENTE)
                .build();

        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario);
        return new TokenResponse(token, usuario.getEmail(), usuario.getNombre(), usuario.getRol().name());
    }

    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generateToken(usuario);
        return new TokenResponse(token, usuario.getEmail(), usuario.getNombre(), usuario.getRol().name());
    }
}