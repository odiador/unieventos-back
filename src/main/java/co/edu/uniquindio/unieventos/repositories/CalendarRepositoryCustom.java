package co.edu.uniquindio.unieventos.repositories;

import java.time.LocalDate;
import java.util.List;

import co.edu.uniquindio.unieventos.model.documents.Calendar;

public interface CalendarRepositoryCustom {
    List<Calendar> findCalendarsWithFilteredEvents(String name, String city, LocalDate date, String tagName, int skip, int limit);
}
