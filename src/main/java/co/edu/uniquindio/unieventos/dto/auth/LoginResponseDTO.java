package co.edu.uniquindio.unieventos.dto.auth;

import co.edu.uniquindio.unieventos.dto.client.UserDataDTO;

public record LoginResponseDTO(UserDataDTO userData, String token) {}
