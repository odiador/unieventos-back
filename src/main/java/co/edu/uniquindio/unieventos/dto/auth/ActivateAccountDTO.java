package co.edu.uniquindio.unieventos.dto.auth;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ActivateAccountDTO(
		@NotNull 
		@Email
		String email,

		@NotNull
		@Length(min = 8, max = 8)
		String code
) {}
