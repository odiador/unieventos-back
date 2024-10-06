package co.edu.uniquindio.unieventos.services.impl;


import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.model.documents.Event;
import co.edu.uniquindio.unieventos.model.enums.EventStatus;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import co.edu.uniquindio.unieventos.services.EventService;

@Service
public class EventServiceImpl implements EventService {
    @Override
    public Event obtenerEvento(String oid) {
    	// TODO change
        return Event.builder().address("Calle 2").city("Armenia").startTime(LocalDateTime.now()).description("El mejor evento que veras en tu vida").name("Laumi").endTime(LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay()).status(EventStatus.ACTIVE).type(EventType.CULTURAL).id(oid).build();
    }
}
