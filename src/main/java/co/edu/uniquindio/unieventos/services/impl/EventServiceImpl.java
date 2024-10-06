package co.edu.uniquindio.unieventos.services.impl;


import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.model.Event;
import co.edu.uniquindio.unieventos.model.EventStatus;
import co.edu.uniquindio.unieventos.model.EventType;
import co.edu.uniquindio.unieventos.services.EventService;

@Service
public class EventServiceImpl implements EventService {
    @Override
    public Event obtenerEvento(String oid) {
        return Event.builder().adress("Calle 2").city("Armenia").date(LocalDate.now().plusDays(10)).description("El mejor evento que veras en tu vida").name("Laumi").endTime(LocalTime.MAX).status(EventStatus.ACTIVE).type(EventType.CULTURAL).id(oid).startTime(LocalTime.MIN).build();
    }
}
