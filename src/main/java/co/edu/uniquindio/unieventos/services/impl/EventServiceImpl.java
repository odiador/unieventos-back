package co.edu.uniquindio.unieventos.services.impl;

import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.config.Mappers;
import co.edu.uniquindio.unieventos.dto.event.CreateEventDTO;
import co.edu.uniquindio.unieventos.dto.event.EditEventDTO;
import co.edu.uniquindio.unieventos.dto.event.EditLocalityDTO;
import co.edu.uniquindio.unieventos.dto.event.EventDTO;
import co.edu.uniquindio.unieventos.dto.event.EventTagDTO;
import co.edu.uniquindio.unieventos.dto.event.EventWCalIdDTO;
import co.edu.uniquindio.unieventos.dto.event.FindEventDTO;
import co.edu.uniquindio.unieventos.dto.event.SearchEventDTO;
import co.edu.uniquindio.unieventos.exceptions.BadRequestException;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.exceptions.MultiErrorException;
import co.edu.uniquindio.unieventos.model.documents.Calendar;
import co.edu.uniquindio.unieventos.model.enums.EventStatus;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import co.edu.uniquindio.unieventos.model.vo.Event;
import co.edu.uniquindio.unieventos.model.vo.EventTag;
import co.edu.uniquindio.unieventos.model.vo.Locality;
import co.edu.uniquindio.unieventos.repositories.CalendarRepository;
import co.edu.uniquindio.unieventos.repositories.CalendarRepositoryCustom;
import co.edu.uniquindio.unieventos.services.EventService;
import co.edu.uniquindio.unieventos.services.ImagesService;
import jakarta.validation.Valid;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private CalendarRepository calendarRepository;
	@Autowired
	private CalendarRepositoryCustom calendarRepositoryCustom;

	@Autowired
	private ImagesService imagesService;
	@Autowired
	private Mappers mappers;

	@Override
	public EventDTO findEvent(@Valid FindEventDTO dto) throws DocumentNotFoundException {
		Calendar calendar = searchCalendar(dto.idCalendar());
		List<Event> events = calendar.getEvents();
		return mappers.getEventMapper().apply(findEventOrThrow(dto.idEvent(), events));
	}

	private Event findEventOrThrow(String id, List<Event> events) throws DocumentNotFoundException {
		for (Event event : events)
			if (id.equals(event.getId()) && event.getStatus() != EventStatus.DELETED)
				return event;
		throw new DocumentNotFoundException("El evento con ese nombre no fue encontrado en tu calendario");
	}

	@Override
	public EventDTO createEvent(@Valid CreateEventDTO dto) throws Exception {
		Calendar calendar = searchCalendar(dto.getIdCalendar());
		String eventImage = dto.getEventImage() != null ?
				imagesService.uploadImage(dto.getEventImage()) : null;
		String localityImage = dto.getLocalityImage() != null ?
				imagesService.uploadImage(dto.getLocalityImage()) : null;
		List<Locality> localities = dto.getLocalities() == null
				? new ArrayList<>()
				: dto.getLocalities().stream()
		    .map(l -> Locality.builder()
		    		.id(new ObjectId().toString())
		    		.name(l.name())
		    		.ticketsSold(0)
		    		.maxCapability(l.maxCapability())
		    		.price(l.price())
		    		.build()
				).collect(Collectors.toList());

		List<EventTag> tags = dto.getTags() == null
				? new ArrayList<EventTag>()
				: dto.getTags().stream()
		    .map(mappers.getDtoToTagMapper()).collect(Collectors.toList());
		Event event = Event.builder()
				.id(new ObjectId().toString())
			    .name(dto.getName())
			    .eventImage(eventImage)
			    .localityImage(localityImage)
			    .city(dto.getCity())
			    .description(dto.getDescription())
			    .address(dto.getAddress())
			    .startTime(LocalDateTime.parse(dto.getStartTime()))
			    .endTime(LocalDateTime.parse(dto.getEndTime()))
			    .localities(localities)
			    .tags(tags)
			    .status(EventStatus.valueOf(dto.getStatus()))
			    .type(EventType.valueOf(dto.getType()))
			    .build();
		calendar.addEvent(event);
		calendarRepository.save(calendar);
		return mappers.getEventMapper().apply(event);
	}


	private Calendar searchCalendar(String idCalendar) throws DocumentNotFoundException {
		return calendarRepository.findById(idCalendar).orElseThrow(
				() -> new DocumentNotFoundException("No se puede encontrar el calendario."));
	}

	@Override
	public EventDTO editEvent(@Valid EditEventDTO dto) throws Exception {
		Calendar calendar = searchCalendar(dto.getIdCalendar());
		Event event = findEventOrThrow(dto.getIdEvent(), calendar.getEvents());

		LocalDateTime start = dto.getStartTime() != null ? LocalDateTime.parse(dto.getStartTime())
				: event.getStartTime();
		LocalDateTime end = dto.getEndTime() != null ? LocalDateTime.parse(dto.getEndTime()) : event.getEndTime();

		if (start.isAfter(end))
			throw new BadRequestException("La fecha de inicio tiene que ser antes que la de fin", "endTime");

		if (dto.getLocalities() != null)
			editLocalities(dto.getLocalities(), event);

	    if (dto.getAddress() != null)
	        event.setAddress(dto.getAddress());

	    if (dto.getCity() != null)
	        event.setCity(dto.getCity());

		if (dto.getDescription() != null)
			event.setDescription(dto.getDescription());

		event.setStartTime(start);
		event.setEndTime(end);

		if (dto.getEventImage() != null) {
	    	String eventImage = imagesService.uploadImage(dto.getEventImage());
	        event.setEventImage(eventImage);
	    }

	    if (dto.getLocalityImage() != null) {
			String localityImage = imagesService.uploadImage(dto.getLocalityImage());
	        event.setLocalityImage(localityImage);
	    }

	    if (dto.getTags() != null) 
	    	editTags(dto.getTags(), event);

	    if (dto.getStatus() != null)
	        event.setStatus(EventStatus.valueOf(dto.getStatus()));

	    if (dto.getType() != null)
	        event.setType(EventType.valueOf(dto.getType()));
		if (dto.getNewName() != null)
			event.setName(dto.getNewName());
		calendar.updateEvent(event);
		calendarRepository.save(calendar);
		return mappers.getEventMapper().apply(event);
	}

	private void editLocalities(List<EditLocalityDTO> localitiesDTO, Event event) throws Exception {
		Map<String, Locality> existingLocs = event.getLocalities().stream()
		        .collect(Collectors.toMap(Locality::getId, locality -> locality));

		List<String> errors = new ArrayList<String>();
		List<Locality> newLocalities = localitiesDTO.stream()
		        .map(l -> {
		            Locality existingLocality = existingLocs.get(l.id());

					if (existingLocality != null) {
						int minEditingCapability = existingLocality.minEditingCapability();
						if (l.maxCapability() < minEditingCapability) {
							errors.add(String.format("El mínimo en la localidad \"%s\" (%s) es: %d", l.name(), l.id(),
									minEditingCapability));
							return null;
						}
						existingLocality.setMaxCapability(l.maxCapability());
						existingLocality.setPrice(l.price());
						return existingLocality;
		            } else 
		                return Locality.builder()
		                	.id(l.id())
		                    .name(l.name())
		                    .ticketsSold(0)
		                    .retention(0)
		                    .maxCapability(l.maxCapability())
		                    .price(l.price())
		                    .build();
		        })
		        .filter(Objects::nonNull)
		        .collect(Collectors.toList());

		if(!errors.isEmpty())
			throw new MultiErrorException("Algunas localidades no pueden ser editadas", errors, 400);
		event.setLocalities(newLocalities);
	}

	private void editTags(List<EventTagDTO> tagsDTO, Event event) throws Exception {
		Map<String, EventTag> existingLocs = event.getTags().stream()
				.collect(Collectors.toMap(EventTag::getName, locality -> locality));

		List<EventTag> newTags = tagsDTO.stream()
				.map((EventTagDTO l) -> {
					EventTag existingTag = existingLocs.get(l.getName());
					
					if (existingTag != null) {
						existingTag.setColor(l.getColor());
						existingTag.setTextColor(l.getTextColor());
						return (EventTag) existingTag;
					} else 
						return EventTag.builder()
								.name(l.getName())
								.color(l.getColor())
								.textColor(l.getTextColor())
								.build();
				})
				.collect(Collectors.toList());
		
		event.setTags(newTags);
	}
	


	@Override
	public List<EventWCalIdDTO> findEvents(SearchEventDTO dto) {
		LocalDateTime date = dto.date() != null ? LocalDateTime.parse(dto.date()) : null;
		List<Calendar> calendars = calendarRepositoryCustom.findCalendarsWithFilteredEvents(
				dto.id(),
				dto.name(),
				dto.city(),
				date,
				dto.tagName(),
				dto.page(),
				dto.size());
		List<SimpleEntry<String, Event>> events = new ArrayList<>();
		for (Calendar calendar : calendars) {
			String id = calendar.getId();
			for (Event event : calendar.getEvents())
				events.add(new SimpleEntry<>(id, event));

		}
		return events.stream()
					.sorted((o1, o2) -> o1.getKey().compareTo(o2.getKey()))
					.map(mappers.getEventCalIdToDTOMapper())
					.collect(Collectors.toList());
	}

	@Override
	public void deleteEvent(@Valid FindEventDTO dto) throws Exception {
		Calendar calendar = searchCalendar(dto.idCalendar());
		Event event = findEventOrThrow(dto.idEvent(), calendar.getEvents());
		event.setStatus(EventStatus.DELETED);
		calendar.updateEvent(event);
		calendarRepository.save(calendar);
	}
}
