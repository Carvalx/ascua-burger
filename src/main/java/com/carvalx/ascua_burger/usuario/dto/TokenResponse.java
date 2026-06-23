package com.carvalx.ascua_burger.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String token;
    private String email;
    private String nombre;
    private String rol;
}