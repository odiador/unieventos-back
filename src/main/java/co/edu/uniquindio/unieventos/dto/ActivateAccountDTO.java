package co.edu.uniquindio.unieventos.dto;

import jakarta.validation.constraints.NotNull;

public record ActivateAccountDTO(
		@NotNull String id,
		@NotNull String code) {
}
