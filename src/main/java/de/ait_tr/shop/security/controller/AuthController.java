package de.ait_tr.shop.security.controller;

import de.ait_tr.shop.security.dto.LoginRequestDto;
import de.ait_tr.shop.security.dto.RefreshRequestDto;
import de.ait_tr.shop.security.dto.TokenResponseDto;
import de.ait_tr.shop.security.service.AuthService;
import jakarta.security.auth.message.AuthException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST - /auth/login
    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody LoginRequestDto loginRequestDto){
        try {
            return authService.login(loginRequestDto);
        } catch (AuthException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/refresh")
    public TokenResponseDto refreshAccessToken(@RequestBody RefreshRequestDto refreshRequestDto){
        try {
            return authService.refreshAccessToken(refreshRequestDto);
        } catch (AuthException e) {
            throw new RuntimeException(e);
        }
    }
}
