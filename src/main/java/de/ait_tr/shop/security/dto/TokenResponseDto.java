package de.ait_tr.shop.security.dto;

import java.util.Objects;

public class TokenResponseDto {

    private String refreshToken;
    private String accessToken;

    public TokenResponseDto(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public TokenResponseDto() {
    }

    @Override
    public String toString() {
        return String.format("Token Response: access Token: %s, refresh Token: %s", accessToken,  refreshToken) ;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenResponseDto that)) return false;
        return Objects.equals(refreshToken, that.refreshToken) && Objects.equals(accessToken, that.accessToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refreshToken, accessToken);
    }
}
