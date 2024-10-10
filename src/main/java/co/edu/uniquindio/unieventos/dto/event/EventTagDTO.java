package co.edu.uniquindio.unieventos.dto.event;

import org.hibernate.validator.constraints.Length;

import co.edu.uniquindio.unieventos.misc.validation.ValidColor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventTagDTO(
		@NotBlank
		@Length(min = 0, max = 20)
		String name, 

		@NotNull
		@ValidColor
		String color, 
		
		@NotNull
		@ValidColor
		String textColor
		
) {}
