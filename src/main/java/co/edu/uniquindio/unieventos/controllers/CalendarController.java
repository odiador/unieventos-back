package co.edu.uniquindio.unieventos.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;

import co.edu.uniquindio.unieventos.dto.calendar.CalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.CreateCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.EditCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.OnlyCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.SearchPageDTO;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface CalendarController {

	ResponseEntity<ResponseDTO<CalendarDTO>> createCalendar(@Valid CreateCalendarDTO dto, HttpServletRequest request)
			throws Exception;

	ResponseEntity<ResponseDTO<CalendarDTO>> editCalendar(@Valid EditCalendarDTO dto, HttpServletRequest request)
			throws Exception;

	ResponseEntity<ResponseDTO<Void>> deleteCalendar(@ValidObjectId @NotNull String id, HttpServletRequest request)
			throws Exception;

	ResponseEntity<ResponseDTO<List<OnlyCalendarDTO>>> searchCalendars(@Valid SearchPageDTO dto) throws Exception;

	ResponseEntity<ResponseDTO<OnlyCalendarDTO>> findCalendar(@Valid String id) throws Exception;

}
