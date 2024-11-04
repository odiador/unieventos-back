package co.edu.uniquindio.unieventos.services;

import co.edu.uniquindio.unieventos.dto.calendar.CalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.CreateCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.EditCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.SearchPageDTO;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface CalendarService {

	CalendarDTO createCalendar(@Valid CreateCalendarDTO dto) throws Exception;

	CalendarDTO findCalendarById(String id) throws Exception;

	CalendarDTO editCalendar(@Valid EditCalendarDTO dto) throws Exception;

	void deleteCalendar(@NotNull String id);

	ResponseDTO<?> searchDalendars(@Valid SearchPageDTO dto);

	ResponseDTO<?> findOnlyCalendar(@Valid String id) throws Exception;

	ResponseDTO<?> findCalendar(@Valid String id) throws Exception;

}
