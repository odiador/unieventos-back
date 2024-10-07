package co.edu.uniquindio.unieventos.dto.auth;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateAccountDTO(
		@NotBlank @Length(min = 0, max = 50) String name,
		@NotBlank @Length(min = 6, max = 11) String cedula,
		@NotBlank @Email String email,
		@NotBlank @Length(min = 0, max = 50) String city,
		@NotBlank @Length(min = 8, max = 15) String password,
		@NotBlank @Length(min = 1, max = 50) String adress,
		@NotBlank @Length(min = 10, max = 10) String phone
) {}
