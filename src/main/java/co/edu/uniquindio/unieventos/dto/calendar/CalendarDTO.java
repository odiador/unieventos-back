package co.edu.uniquindio.unieventos.dto.calendar;

import java.util.List;

import co.edu.uniquindio.unieventos.dto.event.EventDTO;

public record CalendarDTO(
		
		String id,
		String name,
		String description,
		List<EventDTO> events,
		String image,
		String bannerImage

) {}
