package co.edu.uniquindio.unieventos.config;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import co.edu.uniquindio.unieventos.dto.calendar.CalendarDTO;
import co.edu.uniquindio.unieventos.dto.event.EventDTO;
import co.edu.uniquindio.unieventos.dto.event.EventTagDTO;
import co.edu.uniquindio.unieventos.dto.event.ReturnLocalityDTO;
import co.edu.uniquindio.unieventos.model.documents.Calendar;
import co.edu.uniquindio.unieventos.model.vo.Event;
import lombok.Getter;

@Component
@Getter
public class Mappers {

	/**
	 * Maps {@link Event} to {@link EventDTO}
	 */
	private final Function<Event, EventDTO> eventMapper = event -> new EventDTO(
				event.getName(),
				event.getEventImage(),
				event.getLocalityImage(),
				event.getCity(),
				event.getDescription(),
				event.getAddress(),
				event.getStartTime().toString(),
				event.getEndTime().toString(),
				event.getLocalities() == null ? null
					: event.getLocalities()
						.stream()
						.map(locality -> new ReturnLocalityDTO(
								locality.getName(),
								locality.getPrice(),
								locality.getTicketsSold(),
								locality.getMaxCapability()))
						.collect(Collectors.toList()),
			event.getTags() == null ? null
					: event.getTags().stream()
						.map(tag -> new EventTagDTO(
								tag.getName(),
								tag.getColor(),
								tag.getTextColor()))
						.collect(Collectors.toList()),
				event.getStatus(),
				event.getType());

	/**
	 * Maps {@link Calendar} to {@link CalendarDTO}
	 */
	private final Function<Calendar, CalendarDTO> calendarMapper = c -> new CalendarDTO(
			c.getId(),
			c.getName(),
			c.getDescription(),
			c.getEvents()==null?null:c.getEvents().stream()
			.map(eventMapper)
			.collect(Collectors.toList()),
			c.getImage(),
			c.getBannerImage());
}
