package co.edu.uniquindio.unieventos.dto.auth;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(

		@NotBlank @Email String email, 
		@NotBlank @Length(min = 8, max = 15) String password

) {}
