package co.edu.uniquindio.unieventos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateAccountDTO(
		@NotBlank @Max(50) String name,
		@NotBlank @Min(value = 6) @Max(10) String cedula,
		@Email String email,
		@NotBlank @Min(value = 6) @Max(15) String password,
		@NotBlank @Max(50) String adress,
		@NotBlank @Min(10) @Max(10) String phone
) {}
