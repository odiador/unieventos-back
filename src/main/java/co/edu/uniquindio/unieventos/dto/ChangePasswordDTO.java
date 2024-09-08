package co.edu.uniquindio.unieventos.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordDTO(		
		@NotBlank String id,
		@Length(min = 6, max = 6) String passwordCode,
		@Length(min = 8, max = 15) String newPassword,
		@Length(min = 8, max = 15) String confirmPassword
) {}
