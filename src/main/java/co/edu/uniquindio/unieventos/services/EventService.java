package co.edu.uniquindio.unieventos.services;

import java.util.List;

import co.edu.uniquindio.unieventos.dto.event.CreateEventDTO;
import co.edu.uniquindio.unieventos.dto.event.EditEventDTO;
import co.edu.uniquindio.unieventos.dto.event.EventDTO;
import co.edu.uniquindio.unieventos.dto.event.EventWCalIdDTO;
import co.edu.uniquindio.unieventos.dto.event.FindEventDTO;
import co.edu.uniquindio.unieventos.dto.event.SearchEventDTO;
import jakarta.validation.Valid;

public interface EventService {

	EventDTO createEvent(@Valid CreateEventDTO dto) throws Exception;

	EventDTO editEvent(@Valid EditEventDTO dto) throws Exception;

	List<EventWCalIdDTO> findEvents(@Valid SearchEventDTO dto) throws Exception;

	void deleteEvent(@Valid FindEventDTO dto) throws Exception;

	EventDTO findEvent(@Valid FindEventDTO dto) throws Exception;

}
