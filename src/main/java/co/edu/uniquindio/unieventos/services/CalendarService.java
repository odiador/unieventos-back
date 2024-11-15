package co.edu.uniquindio.unieventos.services;

import java.util.List;

import co.edu.uniquindio.unieventos.dto.calendar.CalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.CreateCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.EditCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.OnlyCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.SearchPageDTO;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface CalendarService {

	CalendarDTO createCalendar(@Valid CreateCalendarDTO dto) throws Exception;

	CalendarDTO findCalendarById(String id) throws Exception;

	CalendarDTO editCalendar(@Valid EditCalendarDTO dto) throws Exception;

	void deleteCalendar(@NotNull String id);

	List<OnlyCalendarDTO> searchDalendars(@Valid SearchPageDTO dto);

	OnlyCalendarDTO findOnlyCalendar(@Valid String id) throws Exception;

	CalendarDTO findCalendar(@Valid String id) throws Exception;

}
