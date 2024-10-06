package co.edu.uniquindio.unieventos.dto.auth;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyMailSendDTO(
		@NotBlank @Email String email,
		@NotBlank @Length(min = 8, max = 8) String verificationCode
) {}
