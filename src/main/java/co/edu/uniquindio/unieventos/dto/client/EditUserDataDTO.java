package co.edu.uniquindio.unieventos.dto.client;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EditUserDataDTO(
		@NotBlank
		@Email
		String email,

		@NotBlank
		@Length(min = 0, max = 50)
		String name,
		
		@NotBlank
		@Length(min = 6, max = 10) 
		String cedula,

		@NotBlank 
		@Length(min = 0, max = 50) 
		String adress,

		@NotBlank 
		@Length(min = 0, max = 50)
		String city,

		@NotBlank
		@Length(min = 10, max = 10) 
		String phone
) {}
