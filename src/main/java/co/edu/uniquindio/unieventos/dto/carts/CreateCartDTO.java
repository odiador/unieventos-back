package co.edu.uniquindio.unieventos.dto.carts;

import jakarta.validation.constraints.NotBlank;

public record CreateCartDTO(

		@NotBlank String userId
	) {
}
