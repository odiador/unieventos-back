package co.edu.uniquindio.unieventos.dto.auth;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ChangePasswordDTO(		
		@NotNull 
		@Email
		String email,

		@NotNull
		@Length(min = 6, max = 6)
		String passwordCode,

		@NotNull
		@Length(min = 8, max = 15)
		String newPassword
) {}
