package de.ait_tr.shop.security.service;

import de.ait_tr.shop.model.entity.User;
import de.ait_tr.shop.security.dto.LoginRequestDto;
import de.ait_tr.shop.security.dto.RefreshRequestDto;
import de.ait_tr.shop.security.dto.TokenResponseDto;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserDetailsService userService;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    // username : token
    private final Map<String, String> refreshStorage;

    public AuthService(UserDetailsService userService, TokenService tokenService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.refreshStorage = new HashMap<>();
    }

    public TokenResponseDto login(LoginRequestDto loginRequestDto) throws AuthException {
        UserDetails foundUser = userService.loadUserByUsername(loginRequestDto.username());

        if (passwordEncoder.matches(loginRequestDto.password(), foundUser.getPassword())){
            String accessToken = tokenService.generateAccessToken(foundUser);
            String refreshToken = tokenService.generateRefreshToken(foundUser);

            refreshStorage.put(foundUser.getUsername(), refreshToken);

            return new TokenResponseDto(refreshToken,accessToken);
        }

        throw new AuthException("Incorrect login end / or password");
    }

    /*
    1. Принять данные пользователя
    2. Проверить логина и пароля
    3. Генерация токенов
    4. Сохранить refresh-токин в хранилище
    5. Сохранить ответ
     */


    public TokenResponseDto refreshAccessToken(RefreshRequestDto refreshRequestDto) throws AuthException{
        String token = refreshRequestDto.refreshToken();

        boolean isValid = tokenService.validateRefreshToken(token);

        Claims refreshClaims = tokenService.getRefreshClaims(token);
        String username = refreshClaims.getSubject();

        String sevedToken = refreshStorage.get(username);

        // boolean isSaved = (sevedToken != null && sevedToken.equals(token));
        boolean isSaved = token.equals(sevedToken);

        if (isValid && isSaved){
            UserDetails foundUser = userService.loadUserByUsername(username);

            String accessToken = tokenService.generateAccessToken(foundUser);

            return new TokenResponseDto(token, accessToken);
        }
        throw new AuthException("Incorrect login end / or password");
    }

    /*
    1. Прием данных
    2. Валидация токена в сервисе
    3. Извлечь информацию о пользователе
    4. Проверяем наличие токена в нашем токен Storage
    5. Получить пользователя из базы
    6. Генерируем новый AccessToken
    7. Формирование и возврат ответа
    */


}
