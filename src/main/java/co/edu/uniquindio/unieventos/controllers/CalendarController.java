package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;

import co.edu.uniquindio.unieventos.dto.calendar.CreateCalendarDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public interface CalendarController {

	ResponseEntity<?> createCalendar(@Valid CreateCalendarDTO dto, HttpServletRequest request) throws Exception;
}
