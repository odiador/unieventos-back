package co.edu.uniquindio.unieventos.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;

import co.edu.uniquindio.unieventos.dto.event.CreateEventDTO;
import co.edu.uniquindio.unieventos.dto.event.EditEventDTO;
import co.edu.uniquindio.unieventos.dto.event.EventDTO;
import co.edu.uniquindio.unieventos.dto.event.EventWCalIdDTO;
import co.edu.uniquindio.unieventos.dto.event.FindEventDTO;
import co.edu.uniquindio.unieventos.dto.event.SearchEventDTO;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface EventController {

	ResponseEntity<ResponseDTO<EventDTO>> createEvent(@Valid CreateEventDTO dto, HttpServletRequest request)
			throws Exception;

	ResponseEntity<ResponseDTO<EventDTO>> editEvent(@Valid EditEventDTO dto, HttpServletRequest request)
			throws Exception;

	ResponseEntity<ResponseDTO<EventDTO>> findEvent(@NotNull FindEventDTO dto) throws Exception;

	ResponseEntity<ResponseDTO<List<EventWCalIdDTO>>> getEvents(@Valid SearchEventDTO dto) throws Exception;

	ResponseEntity<ResponseDTO<Void>> deleteEvent(@Valid FindEventDTO dto, HttpServletRequest request) throws Exception;

}
