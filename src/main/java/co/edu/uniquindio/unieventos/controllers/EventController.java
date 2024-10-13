package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;

import co.edu.uniquindio.unieventos.dto.event.CreateEventDTO;
import co.edu.uniquindio.unieventos.dto.event.EditEventDTO;
import co.edu.uniquindio.unieventos.dto.event.FindEventDTO;
import co.edu.uniquindio.unieventos.dto.event.SearchEventDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface EventController {

	ResponseEntity<?> createEvent(@Valid CreateEventDTO dto, HttpServletRequest request) throws Exception;

	ResponseEntity<?> editEvent(@Valid EditEventDTO dto, HttpServletRequest request) throws Exception;

	ResponseEntity<?> findEvent(@NotNull FindEventDTO dto) throws Exception;

	ResponseEntity<?> getEvents(@Valid SearchEventDTO dto) throws Exception;

	ResponseEntity<?> deleteEvent(@Valid FindEventDTO dto, HttpServletRequest request) throws Exception;
}
