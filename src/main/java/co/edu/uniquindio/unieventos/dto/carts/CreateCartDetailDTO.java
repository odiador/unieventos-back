package co.edu.uniquindio.unieventos.dto.carts;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateCartDetailDTO(
		@Min(value = 1) int quantity,
		@NotBlank String localityName,
		@NotBlank String eventId

) {

}
