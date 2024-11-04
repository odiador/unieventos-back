package co.edu.uniquindio.unieventos.dto.calendar;

import jakarta.validation.constraints.Min;

public record SearchPageDTO(
		@Min(0)
		int page,
	
		@Min(1)
		int size,
		
		boolean asc 
		
) {}
