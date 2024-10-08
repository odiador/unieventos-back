package co.edu.uniquindio.unieventos.dto.auth;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecoveryPasswordMailSendDTO(
		@NotBlank @Email String email,
		@NotBlank @Length(min = 6, max = 6) String code
) {}
