package co.edu.uniquindio.unieventos.dto.calendar;

import java.util.List;

import co.edu.uniquindio.unieventos.dto.event.EventTagDTO;


public record OnlyCalendarDTO(
		
		String id,
		String name,
		String description,
		String image,
		String bannerImage,
		List<EventTagDTO> tags

) {}
