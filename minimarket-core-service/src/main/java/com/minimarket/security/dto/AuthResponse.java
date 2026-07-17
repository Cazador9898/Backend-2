package com.minimarket.security.dto;

import java.util.List;

public record AuthResponse(String token, String tipo, long expiraEnSegundos, String username, List<String> roles) {
}
