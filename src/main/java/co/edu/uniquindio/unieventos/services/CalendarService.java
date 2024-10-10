package co.edu.uniquindio.unieventos.services;

import co.edu.uniquindio.unieventos.dto.calendar.CalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.CreateCalendarDTO;
import jakarta.validation.Valid;

public interface CalendarService {

	CalendarDTO createCalendar(@Valid CreateCalendarDTO dto) throws Exception;

	CalendarDTO findCalendarById(String id) throws Exception;

}
