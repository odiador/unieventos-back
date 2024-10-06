package co.edu.uniquindio.unieventos.dto.client;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserDataDTO(

	    @NotBlank String id,
		@NotBlank @Max(50) String name,
		@NotBlank @Min(value = 6) @Max(10) String cedula,
		@NotBlank @Max(50) String adress,
		@NotBlank @Min(10) @Max(10) String phone

) {}
