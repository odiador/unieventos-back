package co.edu.uniquindio.unieventos.dto.calendar;

import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SearchPageDTO(

		@ValidObjectId
		@NotNull
		String id,
		
		@Min(0)
		int page,
	
		@Min(1)
		int size,
		
		@Min(1)
		boolean asc 
		
) {}
