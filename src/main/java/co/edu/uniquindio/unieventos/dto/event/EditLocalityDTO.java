package co.edu.uniquindio.unieventos.dto.event;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EditLocalityDTO(
		

		@NotBlank
		@Length(min = 0, max = 100)
		String name,

		@NotNull
		@Range(min = 0, max = Long.MAX_VALUE)
		Float price,

		@NotNull
		@Range(min = 0, max = Integer.MAX_VALUE)
		Integer maxCapability
		
) {}
