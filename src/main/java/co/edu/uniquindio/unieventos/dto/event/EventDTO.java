package co.edu.uniquindio.unieventos.dto.event;

import java.util.List;

import co.edu.uniquindio.unieventos.model.enums.EventStatus;
import co.edu.uniquindio.unieventos.model.enums.EventType;

public record EventDTO(
		String name,
		String eventImage,
		String localityImage,
		String city, 
		String description,
		String address,
		String startTime,
		String endTime,
		List<ReturnLocalityDTO> localities,
		List<EventTagDTO> tags,
		EventStatus status,
		EventType type

) {}
