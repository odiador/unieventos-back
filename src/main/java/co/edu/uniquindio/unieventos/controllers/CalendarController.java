package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;

import co.edu.uniquindio.unieventos.dto.calendar.CreateCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.EditCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.SearchPageDTO;
import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface CalendarController {

	ResponseEntity<?> createCalendar(@Valid CreateCalendarDTO dto, HttpServletRequest request) throws Exception;

	ResponseEntity<?> editCalendar(@Valid EditCalendarDTO dto, HttpServletRequest request) throws Exception;

	ResponseEntity<?> deleteCalendar(@ValidObjectId @NotNull String id, HttpServletRequest request) throws Exception;

	ResponseEntity<?> searchCalendars(@Valid SearchPageDTO dto) throws Exception;
}
