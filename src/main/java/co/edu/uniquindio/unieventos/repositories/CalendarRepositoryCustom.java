package co.edu.uniquindio.unieventos.repositories;

import java.time.LocalDateTime;
import java.util.List;

import co.edu.uniquindio.unieventos.model.documents.Calendar;

public interface CalendarRepositoryCustom {
	List<Calendar> findCalendarsWithFilteredEvents(String id, String name, String city, LocalDateTime date, String tagName,
			int skip, int limit);
}
