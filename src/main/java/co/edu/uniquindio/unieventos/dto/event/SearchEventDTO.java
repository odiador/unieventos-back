package co.edu.uniquindio.unieventos.dto.event;

import org.hibernate.validator.constraints.Length;

import co.edu.uniquindio.unieventos.misc.validation.ValidDateFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SearchEventDTO(

		@NotNull
		@Min(0)
		Integer page,

		@NotNull
		@Min(0)
		Integer size,

		@Length(min = 0, max = 100)
		String name,

		@Length(min = 0, max = 100)
		String city,
		
		@Length(min = 0, max = 20)
		String tagName,

		@ValidDateFormat
		String date
		
) {}
