package co.edu.uniquindio.unieventos.dto.event;

import org.hibernate.validator.constraints.Length;

public record FilterDTO(
		
		@Length(min = 0,max = 100)
		String name,
		String city,
		String date
		
) {}
