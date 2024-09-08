package co.edu.uniquindio.unieventos.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCartDTO(

		@NotBlank String userId,
		@NotNull List<CreateCartDetailDTO> items
	) {
}
