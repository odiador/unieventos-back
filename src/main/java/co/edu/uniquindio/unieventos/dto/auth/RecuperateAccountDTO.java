package co.edu.uniquindio.unieventos.dto.auth;

import jakarta.validation.constraints.NotNull;

public record RecuperateAccountDTO(
		@NotNull String id,
		@NotNull String code) {
}
