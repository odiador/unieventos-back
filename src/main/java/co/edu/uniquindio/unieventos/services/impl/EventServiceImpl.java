package co.edu.uniquindio.unieventos.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.config.Mappers;
import co.edu.uniquindio.unieventos.dto.event.CreateEventDTO;
import co.edu.uniquindio.unieventos.dto.event.EditEventDTO;
import co.edu.uniquindio.unieventos.dto.event.EditLocalityDTO;
import co.edu.uniquindio.unieventos.dto.event.EventDTO;
import co.edu.uniquindio.unieventos.dto.event.EventTagDTO;
import co.edu.uniquindio.unieventos.dto.event.FindEventDTO;
import co.edu.uniquindio.unieventos.dto.event.SearchEventDTO;
import co.edu.uniquindio.unieventos.exceptions.DocumentFoundException;
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
		return mappers.getEventMapper().apply(findEventOrThrow(dto.name(), events));
	}

	private Event findEventOrThrow(String name, List<Event> events) throws DocumentNotFoundException {
		for (Event event : events)
			if (event.getName().equals(name) && event.getStatus() != EventStatus.DELETED)
				return event;
		throw new DocumentNotFoundException("El evento con ese nombre no fue encontrado en tu calendario");
	}

	@Override
	public EventDTO createEvent(@Valid CreateEventDTO dto) throws Exception {
		Calendar calendar = searchCalendar(dto.getIdCalendar());
		List<Event> events = calendar.getEvents();
		for (Event e : events)
			if (e.getName().equals(dto.getName()))
				throw new DocumentFoundException("Ya creaste un evento con este nombre en el calendario");
		String eventImage = dto.getEventImage() != null ?
				imagesService.uploadImage(dto.getEventImage()) : null;
		String localityImage = dto.getLocalityImage() != null ?
				imagesService.uploadImage(dto.getLocalityImage()) : null;
		List<Locality> localities = dto.getLocalities() == null
				? new ArrayList<>()
				: dto.getLocalities().stream()
		    .map(l -> Locality.builder()
		    		.name(l.name())
		    		.ticketsSold(0)
		    		.maxCapability(l.maxCapability())
		    		.price(l.price())
		    		.build()
				).collect(Collectors.toList());

		List<EventTag> tags = dto.getTags() == null
				? new ArrayList<EventTag>()
				: dto.getTags().stream()
		    .map(t -> EventTag.builder()
		    		.name(t.name())
		    		.color(t.color())
		    		.textColor(t.textColor())
		    		.build() 
				).collect(Collectors.toList());
		Event event = Event.builder()
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
		Calendar calendar = searchCalendar(dto.idCalendar());
		Event event = findEventOrThrow(dto.name(), calendar.getEvents());

		if (dto.localities() != null)
			editLocalities(dto.localities(), event);

	    if (dto.address() != null)
	        event.setAddress(dto.address());

	    if (dto.city() != null)
	        event.setCity(dto.city());

	    if (dto.description() != null)
	        event.setDescription(dto.description());

	    if (dto.startTime() != null)
	        event.setStartTime(LocalDateTime.parse(dto.startTime()));

	    if (dto.endTime() != null) 
	        event.setEndTime(LocalDateTime.parse(dto.endTime()));

	    if (dto.eventImage() != null) {
	    	String eventImage = imagesService.uploadImage(dto.eventImage());
	        event.setEventImage(eventImage);
	    }

	    if (dto.localityImage() != null) {
			String localityImage = imagesService.uploadImage(dto.localityImage());
	        event.setLocalityImage(localityImage);
	    }

	    if (dto.tags() != null) 
	    	editTags(dto.tags(), event);

	    if (dto.status() != null)
	        event.setStatus(EventStatus.valueOf(dto.status()));

	    if (dto.type() != null)
	        event.setType(EventType.valueOf(dto.type()));

		calendar.updateEvent(event);
		calendarRepository.save(calendar);
		return mappers.getEventMapper().apply(event);
	}

	private void editLocalities(List<EditLocalityDTO> localitiesDTO, Event event) throws Exception {
		Map<String, Locality> existingLocs = event.getLocalities().stream()
		        .collect(Collectors.toMap(Locality::getName, locality -> locality));

		List<String> errors = new ArrayList<String>();
		List<Locality> newLocalities = localitiesDTO.stream()
		        .map(l -> {
		            Locality existingLocality = existingLocs.get(l.name());

					if (existingLocality != null) {
						if (existingLocality.getTicketsSold() > l.maxCapability()) {
							errors.add(l.name());
							return null;
						}
						existingLocality.setMaxCapability(l.maxCapability());
						existingLocality.setPrice(l.price());
						return existingLocality;
		            } else 
		                return Locality.builder()
		                    .name(l.name())
		                    .ticketsSold(0)
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
				.map(l -> {
					EventTag existingTag = existingLocs.get(l.name());
					
					if (existingTag != null) {
						existingTag.setColor(l.color());
						existingTag.setTextColor(l.textColor());
						return existingTag;
					} else 
						return EventTag.builder()
								.name(l.name())
								.color(l.color())
								.textColor(l.textColor())
								.build();
				})
				.collect(Collectors.toList());
		
		event.setTags(newTags);
	}
	


	@Override
	public List<EventDTO> findEvents(SearchEventDTO dto) {
		LocalDate date = dto.date()!=null?LocalDate.parse(dto.date()):null;
		List<Calendar> calendars = calendarRepositoryCustom.findCalendarsWithFilteredEvents(
				dto.name(),
				dto.city(),
				date,
				dto.tagName(),
				dto.page(),
				dto.size());
		List<Event> events = new ArrayList<Event>();
		for (Calendar calendar : calendars)
			for (Event event : calendar.getEvents())
				events.add(event);
		return events.stream().map(mappers.getEventMapper()).collect(Collectors.toList());
	}

	@Override
	public void deleteEvent(@Valid FindEventDTO dto) throws Exception {
		Calendar calendar = searchCalendar(dto.idCalendar());
		Event event = findEventOrThrow(dto.name(), calendar.getEvents());
		event.setStatus(EventStatus.DELETED);
		calendar.updateEvent(event);
		calendarRepository.save(calendar);
	}
}
